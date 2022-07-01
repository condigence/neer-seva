import React from "react";
import { StyleSheet, Image, ImageBackground } from "react-native";
import * as Yup from "yup";

import Screen from "../components/Screen";
import {
  AppForm as Form,
  AppFormField as FormField,
  SubmitButton,
} from "../components/forms";

const validationSchema = Yup.object().shape({
  name: Yup.string().required().label("Name"),
  contact: Yup.string().required().min(10).label("Contact"),
  email: Yup.string().email().label("Email"),
});

function RegisterScreen() {
  let image = require('../assets/login_bg.jpg')
  return (  
    <>  
    <ImageBackground source={image} style={styles.image}>
    <Screen style={styles.container}>
    <Image style={styles.logo} source={require("../assets/neerseva.png")} />
      <Form
        initialValues={{ name: "", contact: "", email: "" }}
        onSubmit={(values) => console.log(values)}
        validationSchema={validationSchema}
      >
        <FormField
          autoCorrect={false}
          icon="account"
          name="name"
          placeholder="Name"
        />        
        <FormField
          autoCapitalize="none"
          autoCorrect={false}
          icon="phone"
          name="contact"
          placeholder="Contact"
          // secureTextEntry
          textContentType="telephoneNumber"
        />
        <FormField
          autoCapitalize="none"
          autoCorrect={false}
          icon="email"
          keyboardType="email-address"
          name="email"
          placeholder="Email(Optional)"
          textContentType="emailAddress"
        />
        <SubmitButton title="Register" />
      </Form>
    </Screen>
      </ImageBackground>
    </>

  );
}

const styles = StyleSheet.create({
  container: {
    padding: 10,
    
    
  },
  image: {
    flex: 1,
    resizeMode: "cover",
    justifyContent: "center",
  
  },
  logo: {
    width: 300,
    height: 100,
    alignSelf: "center",
    marginTop: 100,
    marginBottom: 40,
  },
});

export default RegisterScreen;
