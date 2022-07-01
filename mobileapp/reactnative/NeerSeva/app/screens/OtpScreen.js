import React from "react";
import { StyleSheet, Image } from "react-native";
import * as Yup from "yup";

import Screen from "../components/Screen";
import { AppForm, AppFormField, SubmitButton } from "../components/forms";

const validationSchema = Yup.object().shape({
  otp: Yup.string().required().min(4).label("otp") 
});

function OptScreen(props) {
  return (
    <Screen style={styles.container}>
      <Image style={styles.logo} source={require("../assets/neerseva.png")} />

      <AppForm
        initialValues={{ otp: "" }}
        onSubmit={(values) => console.log(values)}
        validationSchema={validationSchema}
      >
        <AppFormField
          autoCapitalize="none"
          autoCorrect={false}         
          keyboardType='keyboardType'
          name="otp"
          placeholder="Enter Your OTP"
          textContentType="telephoneNumber"
        />
        
        <SubmitButton title="Login" />
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

export default OptScreen;
