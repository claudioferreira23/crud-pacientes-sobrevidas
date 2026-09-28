#!/usr/bin/env bash
# Deploy da imagem Docker da API em uma VM via SSH.
# Chamado pelos jobs deploy-staging e deploy-production do workflow ci-cd.yml.
#
# Toda configuração chega por variáveis de ambiente, preenchidas a partir dos
# secrets/variáveis do "environment" do GitHub (staging ou production):
#   IMAGE                imagem a publicar (ex.: ghcr.io/dono/repo:sha-<commit>)
#   CONTAINER_NAME       nome do container na VM (um por ambiente)
#   APP_PORT             porta publicada na VM (padrão 8080)
#   SSH_HOST, SSH_USER, SSH_PRIVATE_KEY, SSH_KNOWN_HOSTS
#   DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD
#   KEYCLOAK_ISSUER_URI
#   GHCR_USER, GHCR_TOKEN  credenciais para a VM baixar a imagem do GHCR
#
# Sem SSH_HOST configurado o script roda em MODO SIMULADO: só registra o que
# faria e sai com sucesso, para o pipeline funcionar antes de existir servidor.
set -euo pipefail

: "${IMAGE:?IMAGE não informada}"
: "${CONTAINER_NAME:?CONTAINER_NAME não informado}"
APP_PORT="${APP_PORT:-8080}"

if [ -z "${SSH_HOST:-}" ]; then
  echo "::warning::SSH_HOST não configurado para este ambiente — deploy SIMULADO."
  echo "Seria executado: docker run -d --name ${CONTAINER_NAME} -p ${APP_PORT}:8080 ${IMAGE}"
  exit 0
fi

: "${SSH_USER:?SSH_USER não configurado}"
: "${SSH_PRIVATE_KEY:?SSH_PRIVATE_KEY não configurado}"
: "${SSH_KNOWN_HOSTS:?SSH_KNOWN_HOSTS não configurado}"
: "${DATABASE_URL:?DATABASE_URL não configurado}"
: "${DATABASE_USERNAME:?DATABASE_USERNAME não configurado}"
: "${DATABASE_PASSWORD:?DATABASE_PASSWORD não configurado}"
: "${KEYCLOAK_ISSUER_URI:?KEYCLOAK_ISSUER_URI não configurado}"

# Chave e known_hosts ficam em diretório temporário removido ao final
WORKDIR="$(mktemp -d)"
trap 'rm -rf "$WORKDIR"' EXIT
umask 077
printf '%s\n' "$SSH_PRIVATE_KEY" > "$WORKDIR/id_deploy"
printf '%s\n' "$SSH_KNOWN_HOSTS" > "$WORKDIR/known_hosts"
SSH_OPTS=(-i "$WORKDIR/id_deploy" -o UserKnownHostsFile="$WORKDIR/known_hosts" -o StrictHostKeyChecking=yes)
TARGET="${SSH_USER}@${SSH_HOST}"
REMOTE_ENV_FILE="\$HOME/${CONTAINER_NAME}.env"

# Arquivo de ambiente da aplicação: sobrescreve application.properties via
# "relaxed binding" do Spring (SPRING_DATASOURCE_URL -> spring.datasource.url).
# É enviado por stdin (nunca aparece na linha de comando nem nos logs).
cat <<EOF | ssh "${SSH_OPTS[@]}" "$TARGET" "umask 077 && cat > ${REMOTE_ENV_FILE}"
SPRING_DATASOURCE_URL=${DATABASE_URL}
SPRING_DATASOURCE_USERNAME=${DATABASE_USERNAME}
SPRING_DATASOURCE_PASSWORD=${DATABASE_PASSWORD}
SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI=${KEYCLOAK_ISSUER_URI}
EOF

# Login no GHCR na VM (token via stdin) para baixar a imagem privada
if [ -n "${GHCR_TOKEN:-}" ]; then
  printf '%s' "$GHCR_TOKEN" | ssh "${SSH_OPTS[@]}" "$TARGET" \
    "docker login ghcr.io -u '${GHCR_USER}' --password-stdin"
fi

# Troca o container: baixa a nova imagem, remove o antigo e sobe o novo
ssh "${SSH_OPTS[@]}" "$TARGET" bash -s <<EOF
set -euo pipefail
docker pull '${IMAGE}'
docker rm -f '${CONTAINER_NAME}' 2>/dev/null || true
docker run -d --name '${CONTAINER_NAME}' --restart unless-stopped \
  --env-file ${REMOTE_ENV_FILE} -p ${APP_PORT}:8080 '${IMAGE}'
docker logout ghcr.io >/dev/null 2>&1 || true
EOF

echo "Deploy de ${IMAGE} concluído em ${SSH_HOST} (${CONTAINER_NAME})."
