import express from "express";
import { createServer } from "http";
import { Server } from "socket.io";

const app = express();
const server = createServer(app);
const io = new Server(server);

server.listen(5000);

let users = [];

const addUser = (userId, socketId) => {
    if (!users.some((user) => user.userId === userId))
        users.push({ userId, socketId });
};

const removeUser = (socketId) => {
    users = users.filter((user) => user.socketId !== socketId);
};

const getUser = (userId) => {
    const user = users.find((user) => user.userId === userId);
    return user;
};

//io.on được sử dụng để lắng nghe các sự kiện từ phía máy chủ
io.on("connection", (socket) => {
    console.log("user connected");

    //socket.on được sử dụng để lắng nghe các sự kiện từ phía khách hàng.
    socket.on("addUser", (userId) => {
        addUser(userId, socket.id);
        //Sử dụng io.emit, nó sẽ phát đi sự kiện đến tất cả các socket đã kết nối đến server.
        io.emit("getUsers", users);
        console.log("sender: " + socket.id);
    });

    socket.on("sendMessage", (m) => {
        const mess = JSON.parse(m);
        const receiver = getUser(mess.receiverId);
        if (receiver) {
            //Sử dụng io.to(socketId).emit. Điều này cho phép chúng ta gửi tin nhắn đến một socket cụ thể,
            // io.to(receiver.socketId).emit("getMessage", {
            //     conversationId,
            //     message,
            // });

            io.to(receiver.socketId).emit("getMessage", {
                conversationId: mess.conversationId,
                message: mess.message,
                receiverId: mess.receiverId,
            });
            console.log(
                "message: " + mess.conversationId + " - " + mess.message
            );
        } else {
            console.log("Receiver is offline");
        }
    });

    socket.on("disconnection", () => {
        console.log("User disconnect");
        removeUser(socket.id);
        io.emit("getUsers", users);
    });
});
