import bcrypt from "bcryptjs";
import User from "../models/User.js";
// import { createToken, isAuth } from "../utils.js";

export const signin = async (req, res) => {
    try {
        const user = await User.findOne({ email: req.body.email });
        res.status(200).send({
            success: true,
            message: "Signin Success",
            result: user,
        });
    } catch (err) {
        res.status(400).send({
            success: false,
            message: "Signin Failure",
            result: err.message,
        });
    }
};

export const signup = async (req, res) => {
    try {
        const user = await User.findOne({ email: req.body.email });
        if (user) {
            await signin(req, res);
            return;
        }
        const newUser = new User({
            name: req.body.name,
            email: req.body.email,
            avatar: req.body.avatar,
        });
        await newUser.save();
        res.status(200).send({
            success: true,
            message: "Signup Success",
            result: newUser,
        });
    } catch (err) {
        res.status(400).send({
            success: false,
            message: "Signup Fail",
            result: err.message,
        });
    }
};
