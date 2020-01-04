import React from 'react';
import {ActivityIndicator, Button, StyleSheet, Text, TextInput, View} from 'react-native';
import {getLogger} from '../core';
import {Consumer} from './context';

const log = getLogger('(auth) Login');

export const Login = ({navigation}) => {
  log('Rendering Login');
  const [username, onChangeUsername] = React.useState('');
  const [password, onChangePassword] = React.useState('');
  return (
    <Consumer>
      {({onLogin, loginError, loginInProgress}) => (
        <View>
          <ActivityIndicator animating={loginInProgress} size='large'/>
          {loginError && <Text>{loginError.message || 'Login error'}</Text>}
          <TextInput
            style={styles.textInput}
            placeholder='Username'
            onChangeText={text => onChangeUsername(text)}
            value={username}
          />
          <TextInput
            style={styles.textInput}
            placeholder="Password"
            onChangeText={text => onChangePassword(text)}
            secureTextEntry={true}
            value={password}
          />
          <Button title="Sign in!" onPress={() => {
            log('Sign in button pressed');
            onLogin(username, password)
              .then(() => {
                log('Navigating to Planes');
                navigation.navigate('Planes')
              });
          }}/>
        </View>
      )}
    </Consumer>
  );
};

Login.navigationOptions = () => ({
  headerTitle: 'Please Login',
});

const styles = StyleSheet.create({
  textInput: {
    height: 40,
    borderColor: 'gray',
    borderWidth: 1,
  },
});
