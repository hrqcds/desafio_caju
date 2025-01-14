import express from "express"
import httpProxy from "express-http-proxy"
import swaggerUI from "swagger-ui-express"
import axios from "axios"
import { init_queue } from "./init_queue"

const swaggerDocument = require('./swagger.json')


const app = express()
const port = 9000

app.use(express.json())

const AccountService = httpProxy("http://account_service:8080")
const TransactionService = httpProxy("http://transaction_service:8081")
const ReportService = httpProxy("http://report_service:8082")



app.use("/accounts", AccountService)

app.use("/transactions", TransactionService)

app.use("/reports", ReportService)

app.use('/api-docs', swaggerUI.serve, swaggerUI.setup(swaggerDocument));

app.listen(port, () => {
    console.log(`Server is running on port ${port}`)

    let count = 0
    const clear = setInterval(()=>{
        if (count < 3) {
            clearInterval(clear)
        }

        init_queue()
        count++
    }, 10000)
})