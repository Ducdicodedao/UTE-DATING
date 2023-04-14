import mongoose from "mongoose";

const messageSchema = new mongoose.Schema({
    sender: {
        type: mongoose.Schema.ObjectId,
        ref: "User",
    },
    content: String,
    sentAt: {
        type: Date,
        default: Date.now,
    },
});

const conversationSchema = mongoose.Schema(
    {
        participants: [
            {
                type: mongoose.Schema.ObjectId,
                ref: "User",
            },
        ],
        message: [messageSchema],
    },
    { timestamps: true }
);

export default mongoose.model("Conversation", conversationSchema);
