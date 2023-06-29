# HelloChat:  Android AI Chat
HelloChat é um aplicativo de bate-papo interativo para Android que utiliza recursos de Inteligência Artificial, como a biblioteca ML Kit do Google, para identificar objetos e textos em imagens, realizar transições e responder perguntas. Além disso, incorpora o Jetpack Compose, a nova recomendação do Google para a construção de interfaces de usuário nativas.

![head_pro_github](https://github.com/git-jr/HelloChat/assets/35709152/555a0ce4-05f8-4e55-8b68-8c9231c07f70)


## 🤖📘 ML Kit 
O ML Kit é uma biblioteca do Google que traz as capacidades de Machine Learning (aprendizado de máquina) para dispositivos móveis. Com o ML Kit, você pode executar tarefas de ML, como reconhecimento de texto, detecção de rostos, detecção de objetos, e muito mais, diretamente no dispositivo do usuário, sem a necessidade de um back-end de ML (sim, podemos rodar isso sem precisa de conexão a internet).

Para saber mais sobre o ML Kit, consulte a [documentação oficial do ML Kit](https://developers.google.com/ml-kit).



## 🚧 Como o aplicativo funciona
### Reconhecimento de imagens
Por agora, o HelloChat permite que os usuários enviem imagens para um bate-papo. O aplicativo, em seguida, usa o ML Kit para identificar objetos na imagem, e exibe os resultados no bate-papo. Veja o GIF abaixo para ver o aplicativo em ação:

https://github.com/git-jr/HelloChat/assets/35709152/bf9928b8-7d12-4629-b42e-d2ca1fb86416

### Auto identificação de idioma
Usando as libs de identificação de idioma e tradução, o app responde sempre com base no idioma da ultima entrada do usuário, o app não tem mauitas respostas ainda, mas para testar enviei uma imagem, depois envie uma mensagem em diferentes idioma e veja você mesmo resultado.

https://github.com/git-jr/HelloChat/assets/35709152/f77ad19d-e0b9-43e3-8ac9-7089cc4ac5a2







‎ㅤ
‎ㅤ
‎ㅤ
## 📱 Instalação e Uso

Para começar a usar o HelloChat, baixe o [arquivo mais recente instável][apk_mais_recente] e execute-o em um dispositivo Android, ou faça um clone do repositório e compile o projeto usando o Android Studio.

🔒 O HelloChat faz uso do [Storage Acess Framework (SAF)](https://developer.android.com/guide/topics/providers/document-provider) uma maneira segura de lidar com os arquivos do Android, ele só tem acesso ao arquivos que você seleciona explicitamente.


‎ㅤ
‎ㅤ
‎ㅤ
‎ㅤ
## 🎨 Jetpack Compose
O Jetpack Compose é um moderno toolkit de construção de UI desenvolvido pela Google para facilitar o desenvolvimento de interfaces de usuário no Android. Ele é projetado para ser simples e intuitivo, permitindo que os desenvolvedores criem interfaces de usuário belas e interativas com menos código e menos complexidade.

Para saber mais sobre o Compose, veja [esse meu artigo na Alura](https://www.alura.com.br/artigos/vale-a-pena-aprender-jetpack-compose) ou consulte a [documentação oficial](https://developer.android.com/jetpack/compose).

## ML Kit + Android
Se você é iniciante no mundo da IA com Android, vale a pena conferir a [documentação oficial de apps mais inteligentes no Android](https://developer.android.com/ml).



😁 Qualquer dúvida ou sugestão, sinta-se à vontade para abrir uma issue ou enviar um pull request. Obrigado por visitar este repositório!


[apk_mais_recente]: https://github.com/git-jr/HelloChat/releases
