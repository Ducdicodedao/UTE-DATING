import express from "express";
import {
  getInfo,
  updateInfo,
  getUsersByDatewith,
  addUserSwipedRight,
  getUserSwipedRight,
  isUserSwipedRight,
  addUserMatched,
  getUsersAvatar,
  getUserMatched,
  unMatched,
  verifyUser,
} from "../controllers/userController.js";

const router = express.Router();

router.get("/getInfo/:userId", getInfo);
router.patch("/updateInfo/:userId", updateInfo);
router.get("/getUsersByDatewith/:userId", getUsersByDatewith);
router.patch("/addUserSwipedRight", addUserSwipedRight);
router.get("/getUserSwipedRight/:userId", getUserSwipedRight);
router.get("/isUserSwipedRight/:userId/:swipedUserId", isUserSwipedRight);
router.patch("/addUserMatched", addUserMatched);
router.get("/getUsersAvatar/:userId/:swipedUserId", getUsersAvatar);
router.get("/getUserMatched/:userId", getUserMatched);
router.patch("/unMatched", unMatched);
router.patch("/verifyUser/:userId", verifyUser);
export default router;
