import React, { useEffect } from "react";
import { FlatList, StyleSheet, Card, View, Text } from "react-native";
import axios from 'axios';

// import Button from "../components/Button";
// import Card from "../components/Card";



import Screen from "../components/Screen";
import colors from "../config/colors";
import { date } from "yup";


function HomeScreen({ navigation }) {
  

    
    const url = 'http://www.neerseva.com/neerseva/api/v1/items';
     
 

    const CallServer = async() => {  
        const {data} = await axios.get(url);            
        return data;            
    } 
    
    
  
     
     
 


  useEffect(() => {
    CallServer();
  }, []);

  return (
    <Screen style={styles.screen}>
       
    <Text>Hellllllll</Text>

       {/* <FlatList
          data={data()}
          keyExtractor={(listing) => listing.id.toString()}
          renderItem={({ item }) => (
            <Card
              title={item.name}
            //   subTitle={"$" + item.price}
            //   imageUrl={item.images[0].url}
            //   onPress={() => navigation.navigate(routes.LISTING_DETAILS, item)}
            //   thumbnailUrl={item.images[0].thumbnailUrl}
            />
          )}
        /> */}
      </Screen>
  );
}

const styles = StyleSheet.create({
  screen: {
    padding: 20,
    backgroundColor: colors.light,
  },
});

export default HomeScreen;
