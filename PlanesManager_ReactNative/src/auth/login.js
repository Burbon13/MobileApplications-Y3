import React from 'react';
import {ActivityIndicator, TouchableOpacity, StyleSheet, Text, TextInput, View} from 'react-native';
import {getLogger} from '../core';
import {Consumer} from './context';

const log = getLogger('Login');

export const Login = ({navigation}) => {
  log('Rendering Login');
  const [username, onChangeUsername] = React.useState('');
  const [password, onChangePassword] = React.useState('');
  return (
    <Consumer>
      {({onLogin, loginError, loginInProgress}) => (
        <View style={styles.container}>
          <TextInput
            style={[styles.formElemMargin, styles.textInput]}
            placeholder='Username'
            onChangeText={text => onChangeUsername(text)}
            value={username}
          />
          <TextInput
            style={[styles.formElemMargin, styles.textInput]}
            placeholder="Password"
            onChangeText={text => onChangePassword(text)}
            secureTextEntry={true}
            value={password}
          />
          <TouchableOpacity
            style={[styles.formElemMargin, styles.button]}
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
              style={[styles.formElemMargin,styles.errorText]}>
              {loginError.message || 'Login error'}
            </Text>
          }
        </View>
      )}
    </Consumer>
  );
};

Login.navigationOptions = () => ({
  headerTitle: 'Please Login',
});

const styles = StyleSheet.create({
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
