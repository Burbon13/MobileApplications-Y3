import React from 'react';
import {ActivityIndicator, TouchableOpacity, StyleSheet, Text, TextInput, View, Animated, Easing} from 'react-native';
import {getLogger} from '../core';
import {AuthContextConsumer} from './context';

const log = getLogger('Login');

export const Login = ({navigation}) => {
  log('Rendering Login');
  const [username, onChangeUsername] = React.useState('');
  const [password, onChangePassword] = React.useState('');

  return (
    <AuthContextConsumer>
      {({onLogin, loginError, loginInProgress}) => (
        <View style={_styles.container}>
          <TextInput
            style={[_styles.formElemMargin, _styles.textInput]}
            placeholder='Username'
            onChangeText={text => onChangeUsername(text)}
            value={username}
          />
          <TextInput
            style={[_styles.formElemMargin, _styles.textInput]}
            placeholder="Password"
            onChangeText={text => onChangePassword(text)}
            secureTextEntry={true}
            value={password}
          />
          <TouchableOpacity
            style={[_styles.formElemMargin, _styles.button]}
            onPress={() => {
              log('Sign in button pressed');
              onLogin(username, password)
                .then(() => {
                  log('Navigating to Planes');
                  navigation.navigate('Planes')
                })
                .catch(error => {
                  log(`Login error ${error}`);
                });
            }}
          >
            <Text>Login</Text>
          </TouchableOpacity>
          <ActivityIndicator
            animating={loginInProgress}
            size='large'/>
          {
            loginError
            &&
            <Text
              style={[_styles.formElemMargin, _styles.errorText]}>
              {loginError.message || 'Login error'}
            </Text>
          }
          <View style={{alignItems: 'center', marginTop: 20}}>
            <Animated.Image
              style={{width: 120, height: 120}}
              source={require('../../assets/images/plane.png')}/>
          </View>
        </View>
      )}
    </AuthContextConsumer>
  );
};

Login.navigationOptions = () => ({
  headerTitle: 'Please Login',
});

const _styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'flex-start',
    paddingHorizontal: 10
  },
  formElemMargin: {
    padding: 10,
    marginHorizontal: 30,
    marginVertical: 10,
  },
  textInput: {
    height: 40,
    borderColor: 'gray',
    borderWidth: 1,
  },
  errorText: {
    color: 'red',
    textAlign: 'center',
    fontSize: 24,
    fontWeight: 'bold',
  },
  button: {
    alignItems: 'center',
    backgroundColor: '#DDDDDD',
  }
});
