import mongoose from "mongoose";

const userSchema = new mongoose.Schema(
    {
        name: {
            type: String,
            trim: true,
        },
        email: {
            type: String,
        },
        facebook: {
            type: String,
        },
        birthday: { type: String, default: "" },

        gender: {
            type: String,
            default: "",
            enum: ["male", "female", ""],
        },
        dateWith: {
            type: String,
            default: "",
            enum: ["male", "female", ""],
        },
        avatar: {
            type: String,
        },
        about: { type: String, default: "" },
        interests: [{ type: String }],
        faculty: {
            type: String,
            default: "",
            enum: [
                "Chính trị - Luật",
                "Cơ khí Chế tạo máy",
                "Điện - Điện tử",
                "Cơ khí Động Lực",
                "Kinh tế",
                "Công nghệ thông tin",
                "In và Truyền thông",
                "Công nghệ May và Thời Trang",
                "Công nghệ Hóa học và Thực phẩm",
                "Xây dựng",
                "Ngoại ngữ",
                "Đào tạo CLC",
                "Đào tạo quốc tế",
                "",
            ],
        },
        isAuthenticated: {
            type: Boolean,
            default: false,
        },
        location: {
            type: {
                type: String, // Don't do `{ location: { type: String } }`
                enum: ["Point"], // 'location.type' must be 'Point'
            },
            coordinates: {
                type: [Number],
            },
        },
        userMatched: [
            {
                type: mongoose.Schema.ObjectId,
                ref: "User",
            },
        ],
        userSwipedRight: [
            {
                type: mongoose.Schema.ObjectId,
                ref: "User",
            },
        ],
    },
    { timestamps: true }
);
export default mongoose.model("User", userSchema);
