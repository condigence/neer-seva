import React from "react";
import { StyleSheet, Image } from "react-native";
import * as Yup from "yup";

import Screen from "../components/Screen";
import { AppForm, AppFormField, SubmitButton } from "../components/forms";

const validationSchema = Yup.object().shape({
  contact: Yup.string().required().min(10).label("Contact"),
  //password: Yup.string().required().min(4).label("Password"),
});

function LoginScreen(props) {
  return (
    <Screen style={styles.container}>
      <Image style={styles.logo} source={require("../assets/neerseva.png")} />

      <AppForm
        initialValues={{ contact: "" }}
        onSubmit={(values) => console.log(values)}
        validationSchema={validationSchema}
      >
        <AppFormField
          autoCapitalize="none"
          autoCorrect={false}
          icon="phone"
          keyboardType='keyboardType'
          name="contact"
          placeholder="Enter Contact Number"
          textContentType="telephoneNumber"
        />
        
        <SubmitButton title="Get OTP" />
      </AppForm>
    </Screen>
  );
}

const styles = StyleSheet.create({
  container: {
    padding: 10,
  },
  logo: {
    width: 300,
    height: 100,
    alignSelf: "center",
    marginTop: 100,
    marginBottom: 100,
  },
});

export default LoginScreen;
