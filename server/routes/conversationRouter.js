import express from "express";

import {
    createConversation,
    getConversationIdByUserId,
    sendMessageToConversation,
    getMessages,
} from "../controllers/conversationController.js";

const router = express.Router();

router.post("/createConversation", createConversation);
router.get(
    "/getConversationIdByUserId/:senderId/:receiverId",
    getConversationIdByUserId
);
router.post("/sendMessage/:conversationId", sendMessageToConversation);
router.get("/getMessages/:conversationId", getMessages);
export default router;
