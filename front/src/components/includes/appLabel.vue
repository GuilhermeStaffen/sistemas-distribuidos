<template>
  <div class="label">
    <div class="title">
      <h2>{{ title }}</h2>
      <p>#{{ id }}</p>
    </div>
    <p>{{ description }}</p>
    <div class="right">
      <button style="background-color: #0c3661;">Compartilhar</button>
      <button style="background-color: #0C611F;">Editar</button>
      <button style="background-color: #7D0F44;">Concluir</button>
    </div>
  </div>
</template>

<script>
export default {
  name: "appLabel",
  props: ["title", "description", "id"],
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

.right{
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
</style>