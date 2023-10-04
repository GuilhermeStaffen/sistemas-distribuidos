<template>
  <div id="container">
    <section class="main">
      <div class="row-95">
        <button v-on:click="newTodo" id="newTodo">Nova tarefa</button>
      </div>
      <div class="row-95">
        <div class="column">
          <h2>Suas Tarefas</h2>
          <section v-if="errored">
            <p>
              Pedimos desculpas, não estamos conseguindo recuperar as
              informações no momento. Por favor, tente novamente mais tarde.
            </p>
          </section>
          <section v-else>
            <div v-if="loading">
              <div class="lds-ripple">
                <div></div>
                <div></div>
              </div>
            </div>
            <ul id="apps" v-else>
              <li v-for="label in info" :key="label.id">
                <appLabel
                  :title="label.title"
                  :description="label.description"
                  :id="label.id"
                />
              </li>
            </ul>
          </section>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import appLabel from "./appLabel.vue";
var axios = require("axios");
import myconfig from "../../myconfig.js";

var config = {
  method: "get",
  url: myconfig.api + "/todos",
  headers: {
    Authorization: "Bearer " + localStorage.token,
  },
};

export default {
  name: "container",
  data() {
    return {
      info: null,
      loading: true,
      errored: false,
    };
  },
  methods: {
    newTodo: function () {
      this.$router.push("/new");
    },
    fetchData: function () {
      axios(config)
        .then((response) => {
          this.info = response.data.activedTodos;
        })
        .catch((error) => {
          this.errored = true;
          if (error.response && error.response.status === 401) {
            this.$router.push("/auth");
          }
        })
        .finally(() => (this.loading = false));
    },
  },
  mounted() {
    this.fetchData();
    setInterval(() => {
      this.fetchData();
    }, 10000); 
  },
  components: {
    appLabel,
  },
};
</script>


<style lang="css" scoped>
#newTodo {
  margin: 50px 0px 10px 0px;
  width:200px;
  padding: 10px;
  color: white;
  background-color: #0c3661;
  font-size: 18px;
  border-radius: 5px;
  border: 1px solid rgba(12, 54, 97, 30%);
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
  margin-top: 10px;
  flex: 95%;
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  padding-left: 5%;
  padding-right: 5%;
}

.column {
  flex: 30%;
  padding: 10px;
  min-height: 5vh;
  margin-bottom: 60px;
  color: #0c3661;
  background-color: #fff;
  border-radius: 10px;
  border: 1px solid rgb(12, 54, 97);
}

h2 {
  padding-bottom: 5px;
  border-bottom: 1px solid #0c3661;
}

@media screen and (max-width: 768px) {
  .main {
    flex-direction: column;
  }
  .row-95 {
    flex-direction: column;
    padding-left: 1px;
    padding-right: 1px;
  }
  .column {
    height: 30vh;
  }
  #apps {
    flex-direction: column;
  }
  h1 {
    font-size: 48px;
  }
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