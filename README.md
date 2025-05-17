# 💪 Fitness Pro Service

Este é o serviço REST principal do ecossistema **Fitness Pro**, desenvolvido em **Kotlin** com **Spring Boot**. Ele foi projetado para ser reutilizado por diferentes aplicativos móveis — Android (Jetpack Compose), Flutter e iOS (Swift) — que compartilham o mesmo domínio fitness, cada um com funcionalidades específicas.

---

## 🧱 Estrutura do Projeto

O projeto é dividido em três módulos:

- **`fitnesspro-core`**: Contém componentes utilitários e genéricos usados por outros módulos.
- **`fitnesspro-service`**: Onde reside toda a lógica de negócio, controladores REST e integrações.
- **`fitnesspro-shared-communication`**: Compartilha estruturas (DTOs, Exceptions, Enums, etc.) com os clientes, e pode ser importado como dependência por apps móveis.

---

## 🔐 Autenticação

A autenticação do serviço é baseada em **JWT** com escopos bem definidos para controle e rastreabilidade:

| Tipo de Token     | Descrição                                                                 | Expiração |
|-------------------|---------------------------------------------------------------------------|-----------|
| 🧩 Aplicação       | Fixo por app. Usado antes do login. Ideal para bootstraps.                | Nunca     |
| 📱 Dispositivo     | Um por instalação. Garante rastreabilidade sem login.                     | 12h       |
| 👤 Usuário         | Token comum de sessão, criado após login/autenticação.                    | 2h        |

Todos os endpoints exigem autenticação. Pelo menos um tipo de token deve estar disponível em qualquer fluxo.

---

## 🔄 Sincronismo de Dados

Para suportar aplicações móveis com funcionamento offline, o serviço foi desenhado com foco em sincronismo eficiente:

- 🔍 **Consultas Paginadas** para evitar sobrecarga de dados.
- 📝 **Persistência em batch** nativa do Spring.
- 🧠 **Cache com corte temporal** baseado na data da última sincronização enviada pelo dispositivo.
- ⏳ Sincronismo a cada ~30 segundos no app, sem interação do usuário.

---

## 📦 Comunicação Cliente/Servidor

O módulo `fitnesspro-shared-communication` é publicado como pacote no **GitHub Packages** e pode ser usado diretamente por qualquer cliente (Android, Flutter, iOS):

- 📤 DTOs para envio e resposta de dados
- ❗ Exceptions padronizadas para erros conhecidos
- 📚 Enums, helpers e contratos

---

## 🔔 Integração com Firebase Cloud Messaging (FCM)

Notificações são parte fundamental da experiência:

- 🔧 **Notificações por regra de negócio** (ex: agendamentos, mensagens).
- ✍️ **Notificações manuais** para avisos administrativos.
- ⚙️ Já implementado via FCM, facilita integração em multiplataforma.

---

## ⏰ Tarefas Agendadas

Processamentos periódicos são tratados com o `TaskScheduler` do Spring:

- 📋 Suporte a criação de jobs configuráveis via JSON.
- 🧩 Arquitetura plugável baseada em interfaces e injeção de dependência.
- 🕒 Agendamento por cron ou período fixo.

---

## 📈 Logs Detalhados

Cada requisição relevante ao serviço gera logs estruturados que incluem:

- 📨 Payload enviado e recebido
- 🧯 Stacktrace em caso de erro
- ⏱️ Tempo de execução no servidor e no client (se enviado)
- 🔁 Rastreabilidade de chamadas frequentes (como sincronismo)

---

## 🗃️ Serialização com Gson

O projeto utiliza **Gson** como padrão para serialização e deserialização de dados, garantindo compatibilidade com clientes móveis. Exceções são feitas apenas para alguns pontos do Spring (ex: Swagger).

---

## 🐳 Infraestrutura

O serviço está **deployado em uma VM SPOT da Google Cloud**, utilizando **Docker** para facilitar o gerenciamento.

- 🌐 Acesso ao Swagger:
  👉 [`https://service.fitnessprotec.com/swagger-ui/index.html`](https://service.fitnessprotec.com/swagger-ui/index.html)

---

## ⚙️ CI/CD - GitHub Actions

O pipeline automatizado executa:

1. 🔄 Build e publicação do módulo `fitnesspro-shared-communication` no GitHub Packages
2. 🏗️ Build da imagem Docker do serviço
3. 🚀 Deploy automático na VM da Google
4. 🏷️ Geração de tag e release no GitHub

---

## 📚 Tecnologias Utilizadas

- [Kotlin](https://kotlinlang.org/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [Spring Web](https://spring.io/guides/gs/rest-service/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Cache](https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#cache)
- [TaskScheduler](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/scheduling/TaskScheduler.html)
- [Firebase Admin SDK](https://firebase.google.com/docs/admin/setup)
- [Gson](https://github.com/google/gson)
- [Docker](https://www.docker.com/)
- [GitHub Actions](https://docs.github.com/en/actions)

---

## 🚀 Próximos Passos

- 📱 Finalizar integração com os apps Flutter e iOS
- 📊 Monitoramento e métricas (Prometheus, Grafana)
- 🔐 Expansão do modelo de permissões

---

## 🧠 Contribuições

Este projeto é de uso pessoal, mas o código é público como parte do meu portfólio. Sugestões são bem-vindas!

---
