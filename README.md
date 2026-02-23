# 🧮 Calculadora Penal

![Kotlin](https://img.shields.io/badge/Kotlin-FF5722?style=for-the-badge&logo=kotlin) ![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android) ![Build](https://img.shields.io/badge/Build-Passing-brightgreen)

Uma aplicação Android desenvolvida em **Kotlin** com interface em **XML** que calcula penas de acordo com parâmetros fornecidos pelo usuário de forma rápida e prática. 
Ideal para estudantes de direito, advogados e profissionais da área jurídica. Foi desenvolvido pelos integrantes da equipe: Arthur, Diego e Luca. Realizado durante
o curso de Desenvolvimento de Aplicativos com a Linguagem Kotlin, com o professor Aparecido Valdemir de Freitas, no período de setembro a novembro de 2025, no qual
aprendemos rapidamente a linguagem de computação Kotlin e desenvolviamos para a empresa Cespedes Lourenço um aplicativo de Calculadora Penal, com os requisitos
exigidos.

---

## 📱 Telas padrão

<table>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/1f81255e-c7c8-4a3f-bdde-83b4b8ed79b9" width="200"/><br>
      Tela Inicial
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/cd5a376a-c90b-4eea-8825-79ac41938d1f" width="200"/><br>
      Botão do Cálculo
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/36aee0db-3f82-4dad-9acb-c73517fe6603" width="200"/><br>
      Salvar e Enviar
    </td>
  </tr>
</table>

---

## ⚙️ Como usar

1. Primeiro mude para o seu idioma de preferência no quadrado superior direito.
2. Insira os dados com relação ao tempo total da pena.
3. Coloque dados sobre a remissão se houver.
4. Aperte em calcular.
5. É mostrado na tela as informações com relação a progressão de pena.
6. Tem a opção de voltar, para caso queira refazer ou tenha digitado algo errado, opção de salvar e enviar, no caso salva a mensagem
e envia em um aplicativo que o usuario desejar os resultados e os campos obrigatórios, e se quiser os opcionais, ou utilize a opção
de "quero falar com um advogado" para ser redirecionado ao contato do advogado da Cespedes Lourenço.
7. Caso deseje entrar em contato direto via e-mail ou via WhatsApp tem as opções com os ícones na tela inicial na inferior direita.

---

## 📱 Telas exemplo

<table>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/6d6d9e47-5e6c-4dec-a999-4b68e36b0af5" width="200"/><br>
      Tempo Total de Pena
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/15cf7618-7c98-4de8-abd4-feb7176a16c9" width="200"/><br>
      Remição
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/d88f2ee6-b14c-4841-9bae-5c12d220edbe" width="200"/><br>
      Resultados
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/13538b24-81ae-40a9-bd62-3a4b07ddaabe" width="200"/><br>
      Salvar e Enviar
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/d8e07d3b-4fe6-4156-b57b-87a3fc01397e" width="200"/><br>
      Botão de G-mail selecionado
    </td>
  </tr>
</table>

---

## ⚙️ Funcionalidades

- Cálculo de pena baseado em:
  - Regime Inicial
  - Tipo de crime
  - Reincidente (opcional)
  - Violência ou grave ameaça (opcional)
  - Crime com resultado morte (opcional)
  - Data de início da pena
  - Dias trabalhados
  - Horas de estudo
  - Livros lidos
  - Tempo de detração (dias)
- Validações de entrada para evitar erros.
- Layout responsivo e intuitivo.
- Envio/armazenamento opcional de histórico de cálculos.
- Tema moderno, limpo e fácil de usar.

---

## 🛠 Tecnologias

- **Linguagem:** Kotlin  
- **Interface:** XML (Layouts Android)  
- **IDE:** Android Studio  
- **Gerenciamento de Dependências:** Gradle  

---

## 🚀 Como Executar

1. Clone o repositório:  
```bash
git clone https://github.com/Arthur199S/CalculadoraPenal.git
```
2. Abra o projeto no Android Studio.

3. Sincronize as dependências do Gradle.

4. Execute o app em um emulador ou dispositivo físico.

---

## 💡 Melhorias Futuras

- Adicionar histórico completo de cálculos salvos (SQLite).

- Suporte a temas claros e escuros (no momento força modo claro).

- Notificações push para alerta de cálculo, quando pronto.

- Faltou a implementação das atividades complementares, mas, como não há uma definição clara sobre como funciona a remição em relação a elas, não foram incluídas.

---

## ⚠️ Desafios e Aprendizados

- Aprender um linguagem de programação nova em pouco tempo de aprendizado dessa nova linguagem.
- Integração de XML + Kotlin (alguns idiomas acusavam erro no código, sendo que era na string o erro).
- A equipe geralmente utiliza VSCode e IntelliJ IDEA; entretanto, foi necessário adaptar-se ao uso do Android Studio para o desenvolvimento deste projeto.

Esses desafios ajudaram a equipe a melhorar a arquitetura do app e otimizar a experiência do usuário.

---

## 📄 Licença

Este projeto está sem licença, ninguém além do autor tem permissão legal para usar, copiar, modificar ou distribuir o código.

Veja para mais detalhes: https://docs.github.com/en/github/creating-cloning-and-archiving-repositories/licensing-a-repository

---

## 🔗 Links Úteis

- [Repositório GitHub](https://github.com/Arthur199S/CalculadoraPenal)  
- [APK para Download](https://drive.google.com/file/d/1pnrrCOkC5E4fMlHThzYN5YGiErM_X5aW/view?usp=sharing)  
- [Documentação de Layouts XML do Android](https://developer.android.com/guide/topics/ui/declaring-layout)  
- [Documentação Kotlin](https://kotlinlang.org/docs/home.html)  
