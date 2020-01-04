import {createStackNavigator} from 'react-navigation-stack';
import {Login} from './login';

export const Auth = createStackNavigator({
  Login: {screen: Login},
});
export * from './auth-loading';
export * from './store';
