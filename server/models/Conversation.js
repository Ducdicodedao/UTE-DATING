import mongoose from "mongoose";

const messageSchema = new mongoose.Schema(
    {
        receiver: {
            type: mongoose.Schema.ObjectId,
            ref: "User",
        },
        content: String,
        sentAt: {
            type: Date,
            default: Date.now,
        },
    },
    { timestamps: true }
);

const conversationSchema = mongoose.Schema(
    {
        participants: [
            {
                type: mongoose.Schema.ObjectId,
                ref: "User",
            },
        ],
        messages: [messageSchema],
    },
    { timestamps: true }
);

export default mongoose.model("Conversation", conversationSchema);
