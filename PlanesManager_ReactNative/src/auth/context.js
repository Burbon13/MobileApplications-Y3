import React from 'react';

export const AuthContext = React.createContext({});

const AuthContextProvider = AuthContext.Provider;
const AuthContextConsumer = AuthContext.Consumer;

export {AuthContextProvider, AuthContextConsumer};
