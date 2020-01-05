import React from 'react';

export const AuthContext = React.createContext({});

const AuthContextProvider = AuthContext.Provider;
const AuthContextConsumer = AuthContext.Provider;

export {AuthContextProvider, AuthContextConsumer};
