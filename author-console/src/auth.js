import Keycloak from 'keycloak-js';
import React from "react";

import {KEYCLOAK_URL} from "./Config";

export const keycloak = new Keycloak({
    url: KEYCLOAK_URL,
    realm: 'yagatalk',
    clientId: 'yagatalk-public-client',
});

try {
    const authenticated = await keycloak.init({
        onLoad: 'login-required',
    });
    console.log(`User is ${authenticated ? 'authenticated' : 'not authenticated'}`);
} catch (error) {
    console.error('Failed to initialize adapter:', error);
}

export const AuthContext = React.createContext({});

export const AuthProvider = ({children}) => {
    return <AuthContext.Provider value={{
        token: keycloak.token,
        userInfo: keycloak.idTokenParsed,
        logout: async () => keycloak.logout()
    }}>
        {children}
    </AuthContext.Provider>
};