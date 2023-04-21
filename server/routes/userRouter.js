import express from "express";
import { updateInfo } from "../controllers/userController.js";

const router = express.Router();

router.patch("/updateInfo/:userId", updateInfo);

export default router;
