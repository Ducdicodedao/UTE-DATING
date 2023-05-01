import User from "../models/User.js";

export const updateInfo = async (req, res, next) => {
    try {
        const user = await User.findOneAndUpdate(
            { _id: req.params.userId },
            { ...req.body },
            { new: true }
        );
        res.status(200).send({
            success: true,
            message: "Update Success",
            result: user,
        });
    } catch (err) {
        console.error(err.message);
        next(err);
    }
};

export const getInfo = async (req, res, next) => {
    try {
        const user = await User.findOne({ _id: req.params.userId });
        res.status(200).send({
            success: true,
            message: "Get Success",
            result: user,
        });
    } catch (err) {
        console.error(err.message);
        next(err);
    }
};

export const getUsersByDatewith = async (req, res, next) => {
    try {
        const user = await User.findOne({ _id: req.params.userId });
        const users = await User.find({
            _id: { $ne: req.params.userId },
            gender: user.dateWith,
            userSwipedRight: { $nin: [req.params.userId] },
            userMatched: { $nin: [req.params.userId] },
        });
        // const result = users.filter((user) => {
        //     return user.userSwipedRight.every(
        //         (swipedUser) => swipedUser._id !== user._id
        //     );
        // });
        res.status(200).send({
            success: true,
            message: "Get Success",
            result: users,
        });
    } catch (err) {
        console.error(err.message);
        next(err);
    }
};

export const isUserSwipedRight = async (req, res, next) => {
    try {
        const user = await User.findOne({ _id: req.params.userId });
        const isSwiped = user.userSwipedRight.includes(req.params.swipedUserId);
        res.status(200).send({
            success: true,
            message: isSwiped ? "isSwipedRight" : "isNotSwipedRight",
        });
    } catch (err) {
        console.error(err.message);
        next(err);
    }
};

export const addUserSwipedRight = async (req, res, next) => {
    try {
        await User.updateOne(
            { _id: req.body.swipedUserId },
            { $push: { userSwipedRight: req.body.userId } }
        );
        res.status(200).send({
            success: true,
            message: "Add Success",
        });
    } catch (err) {
        console.error(err.message);
        next(err);
    }
};

export const addUserMatched = async (req, res, next) => {
    try {
        await User.updateOne(
            { _id: req.body.userId },
            {
                $push: { userMatched: req.body.swipedUserId },
                $pull: { userSwipedRight: req.body.swipedUserId },
            }
        );
        await User.updateOne(
            { _id: req.body.swipedUserId },
            {
                $push: { userMatched: req.body.userId },
            }
        );
        res.status(200).send({
            success: true,
            message: "Add Success",
        });
    } catch (err) {
        console.error(err.message);
        next(err);
    }
};

export const getUserSwipedRight = async (req, res, next) => {
    try {
        const users = await User.findOne({ _id: req.params.userId })
            .select("userSwipedRight")
            .populate({
                path: "userSwipedRight",
                select: "avatar faculty",
            });
        res.status(200).send({
            success: true,
            message: "Get Success",
            result: users,
        });
    } catch (err) {
        console.error(err.message);
        next(err);
    }
};

export const getUsersAvatar = async (req, res, next) => {
    try {
        const avatarUserId = await User.findOne({
            _id: req.params.userId,
        }).select("avatar");
        const avatarSwipedUserId = await User.findOne({
            _id: req.params.swipedUserId,
        }).select("avatar");
        res.status(200).send([avatarUserId.avatar, avatarSwipedUserId.avatar]);
    } catch (err) {
        console.error(err.message);
        next(err);
    }
};

export const getUserMatched = async (req, res, next) => {
    try {
        const users = await User.find({ _id: req.params.userId })
            .select("userMatched")
            .populate({
                path: "userMatched",
                select: "name avatar",
            });
        res.status(200).send({
            success: true,
            message: "Get Success",
            result: users,
        });
    } catch (err) {
        console.error(err.message);
        next(err);
    }
};
