<template>
  <div class="label">
    <div class="title">
      <h2>{{ title }}</h2>
      <p>#{{ id }}</p>
    </div>
    <p>{{ description }}</p>
    <div class="right">
      <button v-on:click="openModal" style="background-color: #0c3661">
        Compartilhar
      </button>
      <button v-on:click="edit" style="background-color: #0c611f">
        Editar
      </button>
      <button v-if="active" v-on:click="done" style="background-color: #7d0f44">
        Concluir
      </button>
      <button  v-else v-on:click="done" style="background-color: #7d0f44">
        Reabrir
      </button>
    </div>
    <transition name="modal">
      <div v-if="modalOpen" class="modal-background">
        <div class="modal-content">
          <h2>Compartilhar tarefa "#{{ id }}"</h2>
          <input
            type="text"
            v-model="email"
            placeholder="E-mail de outro usuário"
          />
          <div class="modal-button">
            <button v-on:click="share(id)" style="background-color: #0c3661">
              Compartilhar
            </button>
            <button v-on:click="closeModal" style="background-color: #7d0f44">
              Fechar
            </button>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
var axios = require("axios");
import myconfig from "../../myconfig.js";

export default {
  name: "appLabel",
  props: ["title", "description", "id", "active"],
  data() {
    return {
      modalOpen: false,
      email: null,
      errors: [],
    };
  },
  methods: {
    openModal() {
      this.modalOpen = true;
    },
    closeModal() {
      this.modalOpen = false;
    },
    validEmail: function (email) {
      var re =
        /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
      return re.test(email);
    },
    share(cod) {
      if (
        this.email == null ||
        this.email == "" ||
        !this.validEmail(this.email) ||
        this.email.length > 50
      ) {
        this.errors.push("Preencha o email com menos de 50 caracteres");
      }

      if (this.errors.length) {
        window.alert("Corrija os seguintes erros: " + this.errors);
      } else {
        var configShare = {
          method: "post",
          url: myconfig.api + "/todos/share",
          headers: {
            Authorization: "Bearer " + localStorage.token,
          },
          data: {
            id: cod,
            email: this.email,
          },
        };
        axios(configShare)
          .then((response) => {
            window.alert(response.data.message);
            this.closeModal();
            this.email = null;
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
    done: function () {
      let endpoint;
      if (this.active) {
        endpoint = "/todos/" + this.id + "/done";
      } else {
        endpoint = "/todos/" + this.id + "/open";
      }
      var config = {
        method: "post",
        url: myconfig.api + endpoint,
        headers: {
          Authorization: "Bearer " + localStorage.token,
        },
      };
      axios(config)
        .then((response) => {
          window.alert(response.data.message);
          this.$router.go(this.$router.currentRoute);
        })
        .catch((error) => {
          if (error.response && error.response.status === 401) {
            window.alert("Erro inesperado, tente novamente");
          } else {
            window.alert(error.response.data.message);
          }
        })
        .finally(() => {});
    },
    edit: function () {
      this.$router.push("/edit/" + this.id);
    },
  },
};
</script>


<style>
button {
  margin: 0px 5px 0px 5px;
  width: 130px;
  padding: 10px;
  color: white;
  font-size: 16px;
  border-radius: 5px;
  border: 1px solid rgba(12, 54, 97, 30%);
}

.label {
  display: flex;
  width: 100%;
  flex-direction: column;
  align-items: flex-start;
  padding: 15px;
  list-style-type: none;
  font-size: 20px;
  background-color: #fff;
  border-radius: 10px;
  border: 1px solid #0c36615d;
  margin: 10px 0px 10px 0px;
  box-shadow: 0px 1px 3px #0c3661;
  color: #0c3661;
}

.title {
  display: flex;
  width: 100%;
  flex-direction: row;
  justify-content: space-between;
}

.right {
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  align-items: flex-end;
}

.title > p {
  padding-right: 20px;
}

.title > h2 {
  font-weight: bold;
}

@media screen and (max-width: 768px) {
  .label {
    width: 100%;
  }
}

.modal-enter-active,
.modal-leave-active {
  transition: opacity 0.5s ease;
}

.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}

.modal-background {
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: #fff;
  border: 1px solid #0c36615d;
  box-shadow: 0px 1px 3px #0c3661;
  padding: 20px;
  border-radius: 10px;
  width: 50%;
}

.modal-button {
  display: flex;
  width: 100%;
  justify-content: flex-end;
  flex-direction: row;
}

input {
  margin: 10px 0px 10px 0px;
  width: 100%;
  padding: 10px;
  border-radius: 10px;
  border: 1px solid rgba(12, 54, 97, 30%);
  font-size: 20px;
}
</style>