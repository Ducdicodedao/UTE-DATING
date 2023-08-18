import User from "../models/User.js";
import Conversation from "../models/Conversation.js";

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
        let users = await User.find({
            _id: { $ne: req.params.userId },
            gender: user.dateWith,
            userSwipedRight: { $nin: [req.params.userId] },
            userMatched: { $nin: [req.params.userId] },
            _id: { $nin: user.userSwipedLeft },
        });
        if (users.length == 0) {
            await User.updateOne(
                { _id: req.body.userId },
                {
                    userSwipedLeft: [],
                }
            );
            users = await User.find({
                _id: { $ne: req.params.userId },
                gender: user.dateWith,
                userSwipedRight: { $nin: [req.params.userId] },
                userMatched: { $nin: [req.params.userId] },
            });
        }
        // Trộn ngẫu nhiên danh sách người dùng
        const shuffledUsers = shuffleArray(users);

        res.status(200).send({
            success: true,
            message: "Get Success",
            result: shuffledUsers,
        });
    } catch (err) {
        console.error(err.message);
        next(err);
    }
};
// Hàm trộn mảng ngẫu nhiên
function shuffleArray(array) {
    const shuffled = array.slice();
    for (let i = shuffled.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [shuffled[i], shuffled[j]] = [shuffled[j], shuffled[i]];
    }
    return shuffled;
}
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

// Thêm id người bị quẹt trái vào tài khoản bản thân
export const addUserSwipedLeft = async (req, res, next) => {
    try {
        await User.updateOne(
            { _id: req.body.userId },
            { $push: { userSwipedLeft: req.body.swipedUserId } }
        );
        res.status(200).send({
            success: true,
            message: "Add UserSwipedLeft Success",
        });
    } catch (err) {
        console.error(err.message);
        next(err);
    }
};

// Thêm id bản thân vào tài khoản người được quẹt phải
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
        const users = await User.findOne({ _id: req.params.userId })
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

export const unMatched = async (req, res, next) => {
    try {
        await Conversation.findOneAndDelete({ _id: req.body.conversationId });
        const updatedUser = await User.findByIdAndUpdate(
            req.body.userId,
            { $pull: { userMatched: req.body.matchedUserId } },
            { new: true }
        );
        const updatedMatchedUser = await User.findByIdAndUpdate(
            req.body.matchedUserId,
            { $pull: { userMatched: req.body.userId } },
            { new: true }
        );
        res.status(200).json({
            success: true,
            message: "UnMatched Success",
        });
    } catch (error) {
        next(error);
    }
};

export const verifyUser = async (req, res) => {
    try {
        await User.findByIdAndUpdate(req.params.userId, {
            isAuthenticated: true,
        });
        res.status(200).json({
            success: true,
            message: "Verify Success",
        });
    } catch (error) {
        next(error);
    }
};
