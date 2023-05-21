import Conversation from "./../models/Conversation.js";

export const createConversation = async (req, res) => {
    try {
        // Create a new conversation
        const conversation = new Conversation({
            participants: [req.body.senderId, req.body.receiverId],
        });
        await conversation.save();
        res.status(200).send({
            success: true,
            message: "Create Conversation Success",
        });
    } catch (err) {
        console.error(err.message);
        next(err);
    }
};

export const getConversationIdByUserId = async (req, res) => {
    try {
        const conversation = await Conversation.findOne({
            participants: {
                $all: [req.params.senderId, req.params.receiverId],
            },
        }).select("_id");
        const { _id } = conversation;
        res.status(200).send(_id);
    } catch (err) {
        console.error(err.message);
        next(err);
    }
};

export const sendMessageToConversation = async (req, res, next) => {
    try {
        // const { sender, message, sendAt } = req.body;
        const { receiver, message } = req.body;

        const sendAt = Date.now();
        const msg = {
            receiver: receiver,
            content: message,
            sentAt: sendAt,
        };
        await Conversation.findByIdAndUpdate(
            { _id: req.params.conversationId },
            { $push: { messages: msg } }
        );
        res.status(200).json({
            success: true,
            message: "Message sent successfully",
        });
    } catch (error) {
        next(error);
    }
};

export const getMessages = async (req, res, next) => {
    // try {
    //     const skip = Number(req.params.skip);
    //     const { message } = await Conversation.findById(req.params.conversationId)
    //         .select({
    //             _id: 0,
    //             message: { $slice: [-(skip + 10), 10] },
    //         });
    //     res.status(200).json(message)
    // } catch (error) {
    //     next(error);
    // }
    try {
        const conversation = await Conversation.findById(
            req.params.conversationId
        ).select("messages");
        const { messages } = conversation;
        res.status(200).json({
            success: true,
            message: "Get Success",
            result: messages,
        });
    } catch (error) {
        next(error);
    }
};

export const getConversationsByUserId = async (req, res, next) => {
    try {
        const conversations = await Conversation.find({
            participants: {
                $in: [req.params.userId],
            },
        }).populate({
            path: "participants",
            select: "_id name avatar token",
        });
        const result = conversations
            .map((conversation) => {
                const lastMessage =
                    conversation.messages[conversation.messages.length - 1];
                //? phương thức toObject() để chuyển đổi đối tượng conversation từ đối tượng Mongoose sang đối tượng JavaScript thuần túy.
                //? Điều này giúp bạn sao chép các trường của đối tượng conversation mà không cần tạo ra các trường bổ sung được thêm vào bởi Mongoose.
                const { messages, ...others } = conversation.toObject();
                return { ...others, lastMessage };
            })
            .filter((conversation) => conversation.lastMessage != null);
        res.status(200).json({
            success: true,
            message: "Get Success",
            result: result,
        });
    } catch (error) {
        next(error);
    }
};

export const getUserMatched = async (req, res, next) => {
    try {
        const conversations = await Conversation.find({
            participants: {
                $in: [req.params.userId],
            },
        }).populate({
            path: "participants",
            select: "_id name avatar token",
        });
        const result = conversations
            .map((conversation) => {
                const lastMessage =
                    conversation.messages[conversation.messages.length - 1];
                //? phương thức toObject() để chuyển đổi đối tượng conversation từ đối tượng Mongoose sang đối tượng JavaScript thuần túy.
                //? Điều này giúp bạn sao chép các trường của đối tượng conversation mà không cần tạo ra các trường bổ sung được thêm vào bởi Mongoose.
                const { messages, ...others } = conversation.toObject();
                return { ...others, lastMessage };
            })
            .filter((conversation) => conversation.lastMessage == null)
            .map((conversation) => {
                conversation.participants = conversation.participants.filter(
                    (participant) => participant._id != req.params.userId
                );
                return conversation;
            });

        res.status(200).json({
            success: true,
            message: "Get Success",
            result: result,
        });
    } catch (error) {
        next(error);
    }
};

export const isExist = async (req, res, next) => {
    try {
        const conversation = await Conversation.findById(
            req.params.conversationId
        );
        let message;
        if (conversation) {
            message = "isExist";
        } else {
            message = "noExist";
        }
        res.status(200).json({
            success: true,
            message,
        });
    } catch (error) {
        next(error);
    }
};
