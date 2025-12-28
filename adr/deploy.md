# Deploy para AWS ECR

O projeto conta com um pipeline de CI/CD configurado para realizar o deploy automático da aplicação no AWS ECR (Elastic Container Registry) sempre que houver um push na branch principal do repositório.

## Configuração do Pipeline
O pipeline de CI/CD está configurado para executar as seguintes etapas:
1. **Build da Imagem Docker**: A aplicação é empacotada em uma imagem Docker utilizando o Dockerfile presente no repositório.
2. **Login no AWS ECR**: A pipeline realiza o login no AWS ECR utilizando as credenciais configuradas nas variáveis de ambiente do serviço de CI/CD.
3. **Push da Imagem para o ECR**: A imagem Docker é enviada para o repositório do AWS ECR.

## Por que Utilizar o AWS ECR?
**Integração com outros serviços AWS**: Facilita a integração com serviços como ECS (Elastic Container Service) e EKS (Elastic Kubernetes Service).