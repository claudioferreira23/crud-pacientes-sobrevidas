#!/usr/bin/env bash
# Smoke test pós-deploy: confirma que a API subiu e que a segurança está ativa.
# Uso: .github/scripts/smoke-test.sh https://staging.exemplo.com
set -euo pipefail

BASE_URL="${1:?Informe a URL base da aplicação}"
BASE_URL="${BASE_URL%/}"
TENTATIVAS="${SMOKE_RETRIES:-30}"
INTERVALO="${SMOKE_INTERVAL:-10}"

status_of() {
  # Em erro de conexão o curl já imprime "000"; "|| true" evita abortar o script
  curl -s -o /dev/null -w '%{http_code}' --max-time 10 -L "$BASE_URL$1" || true
}

# 1) Aguarda a aplicação responder (o container pode levar alguns segundos para subir)
echo "Aguardando ${BASE_URL}/api-docs responder 200..."
for i in $(seq 1 "$TENTATIVAS"); do
  code="$(status_of /api-docs)"
  if [ "$code" = "200" ]; then
    echo "Aplicação no ar (tentativa $i)."
    break
  fi
  if [ "$i" = "$TENTATIVAS" ]; then
    echo "::error::Aplicação não respondeu 200 em /api-docs (último status: $code)."
    exit 1
  fi
  sleep "$INTERVALO"
done

# 2) Verificações de contrato básicas
falhas=0
check() {
  local path="$1" esperado="$2" obtido
  obtido="$(status_of "$path")"
  if [ "$obtido" = "$esperado" ]; then
    echo "OK   GET $path -> $obtido"
  else
    echo "::error::GET $path retornou $obtido (esperado $esperado)"
    falhas=$((falhas + 1))
  fi
}

check /api-docs 200          # documentação OpenAPI pública
check /swagger-ui.html 200   # Swagger UI pública (segue o redirect)
check /pacientes 401         # endpoint protegido: sem token deve negar

[ "$falhas" -eq 0 ] || exit 1
echo "Smoke test concluído com sucesso."
