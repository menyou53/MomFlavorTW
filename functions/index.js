const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);
//this will initialize the firebase app
//here notification is the collection id
//{notification_id} is the Id of Each notification

    exports.sendNotification = functions.firestore.document('notification/{notification_id}').onWrite(event=>{



    const title=event.after.get('title')                            //retrive the title
    const body=event.after.get('body')                              //retrive body

    //create payload regarding notification details
    const payload={                                 //here we create payload as a const
        notification:{
            title:title,                            //assign title in payload title
            body:body                               //assign body in payload title
        }
    }
