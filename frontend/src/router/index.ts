import {
  RouteRecordRaw,
  Router,
  RouterOptions,
  createRouter,
  createWebHashHistory,
} from "vue-router";
import Home from "../views/Home.vue";
import Group from "../views/Group.vue";
import Groups from "../views/Groups.vue";
import UserEntry from "../views/UserEntry.vue";
import { checkUserLogin } from "../service/api/user";

const routes: RouteRecordRaw[] = [
  {
    path: "/",
    name: "home",
    component: Home,
  },
  {
    path: "/groups",
    name: "groups",
    component: Groups,
  },
  {
    path: "/group/:id",
    name: "group",
    component: Group,
  },
  {
    path: "/user/entry",
    name: "user entry",
    component: UserEntry,
  },
];

const options: RouterOptions = {
  history: createWebHashHistory(),
  routes,
};

const router: Router = createRouter(options);

router.beforeEach(async (to, _, next) => {
  const isProtectedRoute = !to.path.includes("/user/entry");
  if (isProtectedRoute) {
    const isAuthenticated = await checkUserLogin();
    if (!isAuthenticated) next({ name: "user entry" });
  }

  next();
});

export default router;
