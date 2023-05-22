import mongoose from "mongoose";

const reportSchema = new mongoose.Schema(
    {
        sender: {
            type: mongoose.Schema.ObjectId,
            ref: "User",
        },
        receiver: {
            type: mongoose.Schema.ObjectId,
            ref: "User",
        },
        isApproved: {
            type: Boolean,
            default: false,
        },
        title: String,
    },
    { timestamps: true }
);

export default mongoose.model("Report", reportSchema);
