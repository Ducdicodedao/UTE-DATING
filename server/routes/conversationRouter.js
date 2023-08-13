import express from "express";

import {
  createConversation,
  getConversationIdByUserId,
  sendMessageToConversation,
  getMessages,
  getMoreMessages,
  getConversationsByUserId,
  getUserMatched,
  isExist,
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
router.get("/getMoreMessages/:conversationId", getMoreMessages);
router.get("/isExist/:conversationId", isExist);
export default router;
