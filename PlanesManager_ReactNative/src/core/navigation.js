import {NavigationActions} from 'react-navigation';

let _navigator;

const setTopLevelNavigator = navigatorRef => _navigator = navigatorRef;

const navigate = (routeName, params) =>
  _navigator.dispatch(
    NavigationActions.navigate({
      routeName,
      params,
    }),
  );

const back = params => _navigator.dispatch(NavigationActions.back(params));

export default {
  back,
  navigate,
  setTopLevelNavigator,
};
