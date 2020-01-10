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
  const [loading, onChangeLoading] = React.useState(false);

  log('FUCK ' + loading);

  const onSubmit = React.useCallback(onLogin => {
    log('Login button pressed');
    onChangeLoading(true);
    onLogin(username, password)
      .then(() => {
        log('Login successful, navigating to Planes');
        navigation.navigate('Planes');
      })
      .catch(error => {
        log(`Login error ${error}`);
        onChangeLoading(false);
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
            style={!loading ?
              [_styles.formElemMargin, _styles.button] :
              [_styles.formElemMargin, _styles.button, _styles.disabledButton]}
            onPress={() => {
              if (!loading) {
                onSubmit(onLogin);
              }
            }}
          >
            <Text style={loading ? _styles.disabledText : {}}>Login</Text>
          </TouchableOpacity>
          <View style={_styles.planeImageWrapper}>
            <PlaneIconAnimation>
              <Image
                style={_styles.planeImage}
                source={require('../../assets/images/plane.png')}/>
            </PlaneIconAnimation>
          </View>
          <ActivityIndicator
            animating={loginInProgress}
            size='large'/>
          {loginError &&
          <Text
            style={[_styles.formElemMargin, _styles.errorText]}>
            {loginError.message || 'Login error'}
          </Text>
          }
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
    alignItems: 'center',
    marginTop: 20,
    marginBottom: 40,
  },
  planeImage: {
    width: 150,
    height: 150,
  },
  disabledButton: {
    backgroundColor: '#c6c7ca',
  },
  disabledText: {
    color: '#9b9c9e',
  },
});
