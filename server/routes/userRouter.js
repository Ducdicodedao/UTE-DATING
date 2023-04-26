import express from "express";
import { getInfo, updateInfo } from "../controllers/userController.js";

const router = express.Router();

router.get("/getInfo/:userId", getInfo);
router.patch("/updateInfo/:userId", updateInfo);

export default router;
