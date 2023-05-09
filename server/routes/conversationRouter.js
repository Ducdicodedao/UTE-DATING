import express from "express";

import {
    createConversation,
    getConversationIdByUserId,
    sendMessageToConversation,
    getMessages,
    getConversationsByUserId,
    getUserMatched,
} from "../controllers/conversationController.js";

const router = express.Router();

router.post("/createConversation", createConversation);
router.get(
    "/getConversationIdByUserId/:senderId/:receiverId",
    getConversationIdByUserId
);
router.get("/getConversationsByUserId/:userId", getConversationsByUserId);
router.get("/getUserMatched/:userId", getUserMatched);
router.post("/sendMessage/:conversationId", sendMessageToConversation);
router.get("/getMessages/:conversationId", getMessages);
export default router;
