import {NavigationActions} from 'react-navigation';

let navigator;

const setTopLevelNavigator = navigatorRef => navigator = navigatorRef;

const navigate = (routeName, params) =>
  navigator.dispatch(
    NavigationActions.navigate({
      routeName,
      params,
    }),
  );

const back = params => navigator.dispatch(NavigationActions.back(params));

export default {
  back,
  navigate,
  setTopLevelNavigator,
};
