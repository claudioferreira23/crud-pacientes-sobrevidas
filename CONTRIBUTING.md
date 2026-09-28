# Como contribuir

Este projeto segue o fluxo de trabalho **GitFlow**:

| Branch | Papel |
| :--- | :--- |
| `main` | Código em produção. Só recebe merge via Pull Request vindo da `dev`. |
| `dev` | Integração (publicada em staging). Só recebe merge via Pull Request vindo de branches de trabalho. |
| branch de trabalho | Uma por mudança, criada a partir da `dev`. |

Nenhuma mudança vai direto para a `main` ou para a `dev`.

## Passo a passo

```bash
git switch dev && git pull origin dev
git switch -c feature/minha-mudanca     # crie sua branch a partir da dev
# ... commits ...
git push -u origin feature/minha-mudanca
```

1. Abra um PR **`sua-branch → dev`**. Com o CI verde e a revisão aprovada, faça o merge (deploy automático em staging).
2. Quando staging estiver validado, abra um PR **`dev → main`** (deploy em produção após aprovação manual).

## Nomes de branches (recomendado, não obrigatório)

| Prefixo | Uso |
| :--- | :--- |
| `feature/` | novas funcionalidades |
| `fix/` | correções de bugs |
| `docs/` | documentação |
| `refactor/` | refatorações sem mudança de comportamento |
| `chore/` | configurações, dependências e manutenção |

O nome é livre. **A única regra validada automaticamente** pelo pipeline (job *Validar branch de origem*) é: **PR para a `main` precisa vir da `dev`**.

## Mensagens de commit

Recomendamos [Conventional Commits](https://www.conventionalcommits.org/pt-br/) em português: `tipo(escopo): descrição no imperativo`, por exemplo `fix(paciente): corrige validação do cep`.

Antes de abrir o PR, rode localmente:

```bash
./mvnw checkstyle:check
./mvnw verify    # requer o PostgreSQL do docker-compose em execução
```

Detalhes do pipeline: [docs/PIPELINE_CI_CD.md](docs/PIPELINE_CI_CD.md).
