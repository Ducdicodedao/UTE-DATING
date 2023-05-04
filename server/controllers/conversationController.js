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
