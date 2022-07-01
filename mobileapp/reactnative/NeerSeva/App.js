import React from "react";
import LoginScreen from "./app/screens/LoginScreen";
import OptScreen from "./app/screens/OtpScreen";
import RegisterScreen from "./app/screens/RegisterScreen";
import WelcomeScreen from "./app/screens/WelcomeScreen";
import {createStackNavigator} from '@react-navigation/stack'
import { NavigationContainer } from '@react-navigation/native';
import AuthNavigator from "./app/navigation/authNavigation";
import HomeScreen from "./app/screens/HomeScreen";



export default function App() {
  return (
    <NavigationContainer>
      <AuthNavigator />
    </NavigationContainer>
    // <HomeScreen  />
  )
}
