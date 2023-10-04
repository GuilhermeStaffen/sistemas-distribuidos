<template>
  <div id="newTodo">
    <Header :showExit="true" />
    <section class="main">
      <div class="row-95">
        <p v-on:click="back">Voltar</p>
        <div class="create">
          <h2>Criar Tarefa</h2>
          <input type="text" v-model="title" placeholder="Título" />
          <textarea
            type="text"
            v-model="description"
            placeholder="Descrição"
            rows="10"
          />
          <button v-on:click="submit">Criar Tarefa</button>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import Header from "./includes/header.vue";
var axios = require("axios");
import myconfig from "../myconfig.js";

export default {
  name: "Home",
  components: {
    Header,
  },
  data() {
    return {
      errors: [],
      title: null,
      description: null,
    };
  },
  methods: {
    back: function () {
      this.$router.back();
    },
    submit: function () {
      this.errors = [];
      if (this.title == null || this.title == "" || this.title.length > 100) {
        this.errors.push("Preencha o título com menos de 50 caracteres");
      }
      if (this.description == null || this.description == "") {
        this.errors.push("Preencha a descrição");
      }
      if (this.errors.length) {
        console.log(this.errors);
        window.alert("Corrija os seguintes erros: " + this.errors);
      } else {
        var config = {
          method: "post",
          url: myconfig.api + "/todos",
          headers: {
            Authorization: "Bearer " + localStorage.token,
          },
          data: {
            title: this.title,
            description: this.description,
          },
        };
        axios(config)
          .then((response) => {
            window.alert(response.data.message);
            this.$router.push("/");
          })
          .catch((error) => {
            if (error.response && error.response.status === 401) {
              window.alert("Erro inesperado, tente novamente");
            } else {
              window.alert(error.response.data.message);
            }
          })
          .finally(() => {});
      }
    },
  },
};
</script>


<style lang="css" scoped>
p {
  margin: 30px 0px 30px 0px;
  cursor: pointer;
  font-size: 20px;
  text-decoration: underline;
}

input {
  margin: 10px 20px 10px 20px;
  width: 60%;
  padding: 10px;
  border-radius: 10px;
  border: 1px solid rgba(12, 54, 97, 30%);
  font-size: 20px;
}

textarea {
  margin: 10px 20px 10px 20px;
  font-size: 20px;
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

.create {
  width: 100%;
  height: 100%;
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