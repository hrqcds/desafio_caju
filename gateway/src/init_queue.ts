import amqp, { Connection, Channel } from 'amqplib/callback_api';

export const init_queue = () => {
    try{
        amqp.connect("amqp://guest:guest@rabbitmq:5672", (error0: any, connection: Connection) => {
            if (error0) {
                throw error0;
            }

            connection.createChannel((error1: any, channel: Channel) => {
                if (error1) {
                    throw error1;
                }

                channel.assertQueue("transactions", {
                    durable: true // A fila será persistente
                });

                console.log(`Fila 'transactions' criada com sucesso!`);

                setTimeout(() => {
                    connection.close();
                }, 1000);

            });
    })

    }catch(e){
        console.log(e)
    }finally{
        console.log("Fim")
    }
}

// (RABBITMQ_URL = "amqp://guest:guest@rabbitmq:5672", QUEUE_NAME = "transactions") => {
//     amqp.connect(RABBITMQ_URL, (error0: any, connection: Connection) => {
//         try {
//             if (error0) {
//                 throw error0;
//             }

            


//             connection.createChannel((error1: any, channel: Channel) => {
//                 if (error1) {
//                     throw error1;
//                 }

//                 channel.assertQueue(QUEUE_NAME, {
//                     durable: true // A fila será persistente
//                 });

//                 console.log(`Fila '${QUEUE_NAME}' criada com sucesso!`);

//                 setTimeout(() => {
//                     connection.close();
//                 }, 500);
//             });
//         } catch (e) {
//             console.log(e)
//         }
//     })
// }