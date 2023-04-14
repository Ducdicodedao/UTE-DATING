import mongoose from "mongoose";

const userSchema = new mongoose.Schema(
    {
        name: {
            type: String,
            trim: true,
            required: [true, "User must have a name"],
        },
        email: {
            type: String,
        },
        password: {
            type: String,
            required: [true, "User must have a password"],
        },
        facebook: {
            type: String,
            required: [true, "User must have a password"],
        },
        birthday: { type: Date },
        gender: {
            type: String,
            enum: ["male", "female", "other"],
        },
        avatar: {
            type: String,
        },
        about: { type: String },
        basics: [{ type: String }],
        interests: [{ type: String }],
        faculty: {
            type: String,
            enum: [
                "Lý luận Chính trị",
                "Khoa học ứng dụng",
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
                "Đào tạo Chất lượng cao",
            ],
        },
        isAuthenticated: {
            type: Boolean,
            default: true,
        },
        loc: {
            location: {
                type: {
                    type: String, // Don't do `{ location: { type: String } }`
                    enum: ["Point"], // 'location.type' must be 'Point'
                    required: true,
                },
                coordinates: {
                    type: [Number],
                    required: true,
                },
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
