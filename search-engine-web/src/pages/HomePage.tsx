import { useState } from 'react';
import { Input, Button, Radio, Space, Card, Tag, Pagination, Empty, Typography } from 'antd';
import { SearchOutlined } from '@ant-design/icons';
import { search } from '../api/search';
import type { HitItem, SearchResponse } from '../api/types';

const { Title, Text } = Typography;

export default function HomePage()
{
    const [keyword, setKeyword] = useState('');
    const [mode, setMode] = useState<'or' | 'and'>('or');
    const [loading, setLoading] = useState(false);
    const [result, setResult] = useState<SearchResponse | null>(null);
    const [page, setPage] = useState(0);
    const pageSize = 10;

    const doSearch = async (p: number = 0) =>
    {
        if (!keyword.trim()) return;
        setLoading(true);
        try {
            const res = await search(keyword.trim(), mode, p, pageSize);
            setResult(res);
            setPage(p);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div>
            <Title level={3} style={{ marginBottom: 24 }}>全文搜索</Title>

            {/* 搜索栏 */}
            <Space.Compact style={{ width: '100%', marginBottom: 16 }}>
                <Input
                    size="large"
                    placeholder="输入搜索关键词（多个词用空格分隔）"
                    value={keyword}
                    onChange={(e) => setKeyword(e.target.value)}
                    onPressEnter={() => doSearch(0)}
                    prefix={<SearchOutlined />}
                />
                <Button type="primary" size="large" loading={loading} onClick={() => doSearch(0)}>
                    搜索
                </Button>
            </Space.Compact>

            <Radio.Group value={mode} onChange={(e) => setMode(e.target.value)} style={{ marginBottom: 24 }}>
                <Radio.Button value="or">OR（包含任一词）</Radio.Button>
                <Radio.Button value="and">AND（包含全部词）</Radio.Button>
            </Radio.Group>

            {/* 结果统计 */}
            {result && (
                <Text type="secondary" style={{ display: 'block', marginBottom: 16 }}>
                    找到 {result.totalHits} 条结果（第 {result.page + 1} 页，共 {Math.ceil(result.totalHits / pageSize)} 页）
                </Text>
            )}

            {/* 结果列表 */}
            {result && result.hits.length === 0 && <Empty description="未找到匹配文档" />}

            {result?.hits.map((hit: HitItem) => (
                <Card
                    key={hit.docId}
                    size="small"
                    style={{ marginBottom: 12 }}
                    title={
                        <Space>
                            <Text strong>{hit.docName}</Text>
                            <Tag color="blue">得分 {hit.score.toFixed(1)}</Tag>
                        </Space>
                    }
                >
                    {/* 高亮片段 */}
                    <div
                        style={{ marginBottom: 8, lineHeight: 1.8, color: '#555' }}
                        dangerouslySetInnerHTML={{ __html: hit.snippet }}
                    />

                    {/* 命中词频 */}
                    <Space wrap>
                        {Object.entries(hit.termFreqMap).map(([term, freq]) => (
                            <Tag key={term} color="green">
                                {term} × {freq}
                            </Tag>
                        ))}
                    </Space>
                </Card>
            ))}

            {/* 分页 */}
            {result && result.totalHits > pageSize && (
                <Pagination
                    current={page + 1}
                    pageSize={pageSize}
                    total={result.totalHits}
                    onChange={(p) => doSearch(p - 1)}
                    showSizeChanger={false}
                    style={{ textAlign: 'center', marginTop: 24 }}
                />
            )}
        </div>
    );
}
