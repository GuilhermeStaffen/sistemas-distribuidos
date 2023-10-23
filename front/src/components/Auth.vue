<template>
  <div id="auth">
    <Header :showExit="false" />
    <section class="main">
      <div class="row-95">
        <div class="row">
          <div class="signin">
            <h2>Entre</h2>
            <input type="email" v-model="emailLogin" placeholder="Email" />
            <input
              type="password"
              v-model="passwordLogin"
              placeholder="Senha"
            />
            <button v-on:click="submitSignin">Entrar</button>
          </div>
          <div class="signup">
            <h2>Cadastre-se</h2>
            <input type="text" v-model="userSignup" placeholder="Usuário" />
            <input type="email" v-model="emailSignup" placeholder="Email" />
            <input
              type="password"
              v-model="passwordSignup"
              placeholder="Senha"
            />
            <button v-on:click="submitSignup">Cadastrar</button>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
var axios = require("axios");
import myconfig from "../myconfig.js";
import Header from "./includes/header.vue";

export default {
  name: "Auth",
  components: {Header},
  data() {
    return {
      errors: [],
      emailLogin: null,
      passwordLogin: null,
      userSignup: null,
      emailSignup: null,
      passwordSignup: null,
    };
  },
  methods: {
    submitSignup: function () {
      this.errors = [];
      if (
        this.userSignup == null ||
        this.userSignup == "" ||
        this.userSignup.length < 3 ||
        this.userSignup.length > 20
      ) {
        this.errors.push(
          "Preencha o usuário com mais de 3 caracteres e menos que 20"
        );
      }

      if (
        this.emailSignup == null ||
        this.emailSignup == "" ||
        !this.validEmail(this.emailSignup) ||
        this.emailSignup.length > 50
      ) {
        this.errors.push("Preencha o email com menos de 50 caracteres");
      }

      if (
        this.passwordSignup == null ||
        this.passwordSignup == "" ||
        this.passwordSignup.length < 6 ||
        this.passwordSignup.length > 40
      ) {
        this.errors.push(
          "Preencha a senha com mais de 6 caracteres e menos que 40"
        );
      }

      if (this.errors.length) {
        window.alert("Corrija os seguintes erros: " + this.errors);
      } else {
        var config = {
          method: "post",
          url: myconfig.api + "/auth/signup",
          data: {
            username: this.userSignup,
            email: this.emailSignup,
            password: this.passwordSignup,
          },
        };
        axios(config)
          .then((response) => {
            window.alert(response.data.message);
          })
          .catch((error) => {
            if (error.response && error.response.status === 401) {
              window.alert("Erro inesperado");
            } else {
              window.alert(error.response.data.message);
            }
          })
          .finally(() => {});
      }
    },
    submitSignin: function () {
      this.errors = [];
      if (
        this.emailLogin == null ||
        this.emailLogin == "" ||
        !this.validEmail(this.emailLogin) ||
        this.emailLogin.length > 50
      ) {
        this.errors.push("Preencha o email com menos de 50 caracteres");
      }

      if (
        this.passwordLogin == null ||
        this.passwordLogin == "" ||
        this.passwordLogin.length < 6 ||
        this.passwordLogin.length > 40
      ) {
        this.errors.push(
          "Preencha a senha com mais de 6 caracteres e menos que 40"
        );
      }

      if (this.errors.length) {
        window.alert("Corrija os seguintes erros: " + this.errors);
      } else {
        var config = {
          method: "post",
          url: myconfig.api + "/auth/signin",
          data: {
            email: this.emailLogin,
            password: this.passwordLogin,
          },
        };
        axios(config)
          .then((response) => {
            localStorage.token = response.data.accessToken;
            localStorage.username = response.data.username;
            this.$router.push("/");
          })
          .catch((error) => {
            if (error.response && error.response.status === 401 && error.response.data.message == 'Bad credentials') {
              window.alert("Credenciais incorretas");
            }
            else if (error.response && error.response.status === 401) {
              window.alert("Erro inesperado");
            } else {
              window.alert(error.response.data.message);
            }
          })
          .finally(() => {});
      }
    },
    validEmail: function (email) {
      var re =
        /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
      return re.test(email);
    },
  },
};
</script>

<style lang="css" scoped>
input {
  margin: 10px 20px 10px 20px;
  width: 60%;
  padding: 10px;
  border-radius: 10px;
  border: 1px solid rgba(12, 54, 97, 30%);
}

button {
  margin: 10px 20px 10px 20px;
  width: 60%;
  padding: 10px;
  color: white;
  background-color: #0c3661;
  font-size: 18px;
  border-radius: 10px;
  border: 1px solid rgba(12, 54, 97, 30%);
}

button:hover {
  background-color: #0c3661e4;
  font-size: 18px;
  border-radius: 10px;
  border: 1px solid rgba(12, 54, 97, 30%);
}

.signin {
  width: 50%;
  height: 100%;
  border-right: 1px solid rgba(12, 54, 97, 30%);
  display: flex;
  flex-direction: column;
  align-items: center;
}
.signup {
  width: 50%;
  height: 100%;
  border-left: 1px solid rgba(12, 54, 97, 30%);
  display: flex;
  flex-direction: column;
  align-items: center;
}

.main {
  padding-top: 9vh;
  min-height: 40vh;
  display: flex;
  flex-direction: column;
  flex-wrap: wrap;
  width: 100%;
}

.row-95 {
  margin-top: 5vh;
  flex: 95%;
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  padding-left: 10%;
  padding-right: 10%;
}

.row {
  margin: 10px;
  flex: 30%;
  padding: 30px 10px 30px 10px;
  min-height: 5vh;
  margin-bottom: 60px;
  color: #0c3661;
  background-color: #fff;
  display: flex;
  flex-direction: row;
  border-radius: 25px;
  border: 1px solid rgba(12, 54, 97, 30%);
}

h2 {
  padding-bottom: 5px;
}
</style>