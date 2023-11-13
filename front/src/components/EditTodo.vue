<template>
  <div id="newTodo">
    <Header :showExit="true" />
    <section class="main">
      <div class="row-95">
        <p v-on:click="back">Voltar</p>
        <div class="create">
          <h2>Editar Tarefa {{ this.$route.params.id }}</h2>
          <p v-on:click="reloading">Atualizar</p>

          <input type="text" v-model="title" placeholder="Título" />
          <textarea
            type="text"
            v-model="description"
            placeholder="Descrição"
            rows="10"
          />
          <button v-on:click="submit">Editar Tarefa</button>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import Header from "./includes/header.vue";
var axios = require("axios");
//import myconfig from "../myconfig.js";

export default {
  name: "Edit",
  components: {
    Header,
  },
  data() {
    return {
      errors: [],
      title: null,
      description: null,
      changeDate: null,
      loading: true,
    };
  },
  mounted() {
    this.reloading();
  },
  methods: {
    reloading: function () {
      var configGet = {
        method: "get",
        url: "http://localhost:9090/todos/" + this.$route.params.id,
        headers: {
          Authorization: "Bearer " + localStorage.token,
        },
      };
      axios(configGet)
        .then((response) => {
          this.title = response.data.title;
          this.description = response.data.description;
          this.changeDate = response.data.openDate;
        })
        .catch((error) => {
          if (error.response && error.response.status === 401) {
            window.alert("Erro inesperado, tente novamente");
          } else {
            window.alert(error.response.data.message);
          }
        })
        .finally(() => {
          this.loading = false;
        });
    },

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
        window.alert("Corrija os seguintes erros: " + this.errors);
      } else {
        var config = {
          method: "post",
          url: "http://localhost:9090/todos/" + this.$route.params.id,
          headers: {
            Authorization: "Bearer " + localStorage.token,
          },
          data: {
            title: this.title,
            description: this.description,
            changeDate: this.changeDate,
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

.lds-ripple {
  display: inline-block;
  position: relative;
  width: 80px;
  height: 80px;
}
.lds-ripple div {
  position: absolute;
  border: 4px solid #0c3661;
  opacity: 1;
  border-radius: 50%;
  animation: lds-ripple 1s cubic-bezier(0, 0.2, 0.8, 1) infinite;
}
.lds-ripple div:nth-child(2) {
  animation-delay: -0.5s;
}
@keyframes lds-ripple {
  0% {
    top: 36px;
    left: 36px;
    width: 0;
    height: 0;
    opacity: 0;
  }
  4.9% {
    top: 36px;
    left: 36px;
    width: 0;
    height: 0;
    opacity: 0;
  }
  5% {
    top: 36px;
    left: 36px;
    width: 0;
    height: 0;
    opacity: 1;
  }
  100% {
    top: 0px;
    left: 0px;
    width: 72px;
    height: 72px;
    opacity: 0;
  }
}
</style>