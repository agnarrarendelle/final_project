import { createApp } from "vue";
import "./style.css";
import App from "./App.vue";
import "bootstrap";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import axios from "./service/index";
import VueAxios from "vue-axios";
import 'flowbite';
import router from "./router"
import { store } from "./state";

const app = createApp(App);

app.use(store);
app.use(ElementPlus);
app.use(router);
app.use(VueAxios, axios);

app.mount("#app");
