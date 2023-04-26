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
