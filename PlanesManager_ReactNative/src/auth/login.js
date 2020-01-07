import React from 'react';
import {ActivityIndicator, TouchableOpacity, StyleSheet, Text, TextInput, View, Image} from 'react-native';
import {getLogger, navigation} from '../core';
import {AuthContextConsumer} from './context';
import {PlaneIconAnimation} from './animations/plane-icon-animation';

const log = getLogger('Login');

export const Login = () => {
  log('Rendering');

  const [username, onChangeUsername] = React.useState('');
  const [password, onChangePassword] = React.useState('');

  const onSubmit = React.useCallback(onLogin => {
    log('Login button pressed');
    onLogin(username, password)
      .then(() => {
        log('Login successful, navigating to Planes');
        navigation.navigate('Planes');
      })
      .catch(error => {
        log(`Login error ${error}`);
      });
  }, [username, password]);

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
            onPress={() => onSubmit(onLogin)}
          >
            <Text>Login</Text>
          </TouchableOpacity>
          <ActivityIndicator
            animating={loginInProgress}
            size='large'/>
          {loginError &&
          <Text
            style={[_styles.formElemMargin, _styles.errorText]}>
            {loginError.message || 'Login error'}
          </Text>
          }
          <View style={_styles.planeImageWrapper}>
            <PlaneIconAnimation>
              <Image
                style={_styles.planeImage}
                source={require('../../assets/images/plane.png')}/>
            </PlaneIconAnimation>
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
    paddingHorizontal: 10,
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
  },
  planeImageWrapper: {
    alignItems: 'center', marginTop: 20,
  },
  planeImage: {
    width: 150,
    height: 150,
  },
});
