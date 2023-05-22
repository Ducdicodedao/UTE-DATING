import express from "express";
import { sendReport, checkReport } from "../controllers/reportController.js";

const router = express.Router();

router.post("/sendReport", sendReport);
router.get("/checkReport/:userId", checkReport);
export default router;
