import { getToken } from "src/utils/auth";
import { LoadingBar, Notify } from "quasar";

export default ({ router, store }) => {
  router.beforeEach(async (to, from, next) => {
    const hasToken = getToken();
    if (hasToken) {
      if (to.path === "/login" || to.path === '/') {
        next({path: "/mainLayout/graph"});
      }else{
        next()
      }
    } else {
      if (!to.meta.isPublic) {
        next({ path: "/login", query: { redirect: to.fullPath } });
      } else {
        next();
      }
    }
  });
  router.afterEach(() => {
    LoadingBar.stop();
  });
};
