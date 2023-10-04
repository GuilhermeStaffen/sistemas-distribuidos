import Vue from 'vue';
import VueRouter from 'vue-router';
Vue.use(VueRouter);

let router = new VueRouter({
    mode: "history",
    routes:[
        {path:'/', name:'home', component: () =>import('./components/Home')},
        {path:'/auth', name:'auth', component: () =>import('./components/Auth')},
        {path:'/new', name:'new', component: () =>import('./components/NewTodo')},
    ]
});

export default router;