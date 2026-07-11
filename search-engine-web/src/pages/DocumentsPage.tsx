import { useEffect, useState } from 'react';
import { Upload, Button, Table, Popconfirm, message, Typography, Space, Card } from 'antd';
import { UploadOutlined, DeleteOutlined, ReloadOutlined } from '@ant-design/icons';
import type { ColumnsType } from 'antd/es/table';
import { listDocuments, uploadDocument, deleteDocument } from '../api/search';
import type { DocInfo } from '../api/types';

const { Title } = Typography;

export default function DocumentsPage()
{
    const [docs, setDocs] = useState<DocInfo[]>([]);
    const [loading, setLoading] = useState(false);

    const fetchDocs = async () =>
    {
        setLoading(true);
        try {
            const data = await listDocuments();
            setDocs(data);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => { fetchDocs(); }, []);

    const handleUpload = async (file: File) =>
    {
        try {
            await uploadDocument(file);
            message.success(`上传成功: ${file.name}`);
            fetchDocs();
        } catch {
            message.error(`上传失败: ${file.name}`);
        }
        return false; // 阻止默认上传行为
    };

    const handleDelete = async (docId: number) =>
    {
        await deleteDocument(docId);
        message.success('已删除');
        fetchDocs();
    };

    const columns: ColumnsType<DocInfo> = [
        { title: 'ID', dataIndex: 'docId', key: 'docId', width: 80 },
        { title: '文件名', dataIndex: 'docName', key: 'docName', ellipsis: true },
        { title: '词条数', dataIndex: 'termCount', key: 'termCount', width: 100 },
        {
            title: '操作',
            key: 'action',
            width: 100,
            render: (_, record) => (
                <Popconfirm title="确定删除？" onConfirm={() => handleDelete(record.docId)}>
                    <Button type="link" danger icon={<DeleteOutlined />}>删除</Button>
                </Popconfirm>
            ),
        },
    ];

    return (
        <div>
            <Title level={3} style={{ marginBottom: 24 }}>文档管理</Title>

            <Card style={{ marginBottom: 24 }}>
                <Space>
                    <Upload
                        accept=".txt"
                        showUploadList={false}
                        beforeUpload={(file) => { handleUpload(file); return false; }}
                    >
                        <Button type="primary" icon={<UploadOutlined />}>上传 .txt 文档</Button>
                    </Upload>
                    <Button icon={<ReloadOutlined />} onClick={fetchDocs}>刷新列表</Button>
                </Space>
            </Card>

            <Table
                columns={columns}
                dataSource={docs}
                rowKey="docId"
                loading={loading}
                pagination={{ pageSize: 10 }}
                locale={{ emptyText: '暂无文档，请上传 .txt 文件' }}
            />
        </div>
    );
}
