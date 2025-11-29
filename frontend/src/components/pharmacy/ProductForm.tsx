import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import { useToast } from '../../contexts/ToastContext';
import './ProductForm.css';

interface Brand {
  id: number;
  name: string;
}

interface Manufacturer {
  id: number;
  name: string;
}

interface Category {
  id: number;
  name: string;
}

interface ProductVariant {
  sku: string;
  dosage: string;
  packageSize: string;
  gtin?: string;
}

interface ProductFormData {
  name: string;
  description: string;
  anvisaCode: string;
  activePrinciple: string;
  isPrescriptionRequired: boolean;
  controlledSubstanceList: string;
  brandId: number | null;
  manufacturerId: number | null;
  variants: ProductVariant[];
  categoryIds: number[];
}

interface ProductFormProps {
  product?: any;
  onClose: () => void;
  onSuccess: () => void;
}

const ProductForm: React.FC<ProductFormProps> = ({ product, onClose, onSuccess }) => {
  const { showToast } = useToast();
  const [formData, setFormData] = useState<ProductFormData>({
    name: product?.name || '',
    description: product?.description || '',
    anvisaCode: product?.anvisaCode || '',
    activePrinciple: product?.activePrinciple || '',
    isPrescriptionRequired: product?.isPrescriptionRequired || false,
    controlledSubstanceList: product?.controlledSubstanceList || '',
    brandId: product?.brandId || null,
    manufacturerId: product?.manufacturerId || null,
    variants: product?.variants?.map((v: any) => ({
      sku: v.sku || '',
      dosage: v.dosage || '',
      packageSize: v.packageSize || '',
      gtin: v.gtin || ''
    })) || [{ sku: '', dosage: '', packageSize: '', gtin: '' }],
    categoryIds: product?.categoryIds || []
  });

  const [brands, setBrands] = useState<Brand[]>([]);
  const [manufacturers, setManufacturers] = useState<Manufacturer[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [loadingData, setLoadingData] = useState(true);

  useEffect(() => {
    loadFormData();
  }, []);

  const loadFormData = async () => {
    try {
      const [brandsRes, manufacturersRes, categoriesRes] = await Promise.all([
        api.get('/brands'),
        api.get('/manufacturers'),
        api.get('/categories')
      ]);
      setBrands(brandsRes.data);
      setManufacturers(manufacturersRes.data);
      setCategories(categoriesRes.data);
    } catch (err: any) {
      console.error('Erro ao carregar dados:', err);
      setError('Erro ao carregar dados do formulário');
    } finally {
      setLoadingData(false);
    }
  };

  const handleInputChange = (field: keyof ProductFormData, value: any) => {
    setFormData(prev => ({ ...prev, [field]: value }));
  };

  const handleVariantChange = (index: number, field: keyof ProductVariant, value: string) => {
    const newVariants = [...formData.variants];
    newVariants[index] = { ...newVariants[index], [field]: value };
    setFormData(prev => ({ ...prev, variants: newVariants }));
  };

  const addVariant = () => {
    setFormData(prev => ({
      ...prev,
      variants: [...prev.variants, { sku: '', dosage: '', packageSize: '', gtin: '' }]
    }));
  };

  const removeVariant = (index: number) => {
    if (formData.variants.length > 1) {
      const newVariants = formData.variants.filter((_, i) => i !== index);
      setFormData(prev => ({ ...prev, variants: newVariants }));
    }
  };

  const handleCategoryToggle = (categoryId: number) => {
    setFormData(prev => ({
      ...prev,
      categoryIds: prev.categoryIds.includes(categoryId)
        ? prev.categoryIds.filter(id => id !== categoryId)
        : [...prev.categoryIds, categoryId]
    }));
  };

  const validateForm = (): boolean => {
    if (!formData.name.trim()) {
      setError('Nome do produto é obrigatório');
      return false;
    }
    if (!formData.activePrinciple.trim()) {
      setError('Princípio ativo é obrigatório');
      return false;
    }
    if (!formData.controlledSubstanceList.trim()) {
      setError('Lista de substâncias controladas é obrigatória');
      return false;
    }
    if (formData.variants.some(v => !v.sku || !v.dosage || !v.packageSize)) {
      setError('Todas as variantes devem ter SKU, dosagem e tamanho da embalagem');
      return false;
    }
    return true;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    if (!validateForm()) {
      return;
    }

    setLoading(true);
    try {
      const requestData = {
        name: formData.name,
        description: formData.description || null,
        anvisaCode: formData.anvisaCode || null,
        activePrinciple: formData.activePrinciple,
        isPrescriptionRequired: formData.isPrescriptionRequired,
        controlledSubstanceList: formData.controlledSubstanceList,
        brandId: formData.brandId || null,
        manufacturerId: formData.manufacturerId || null,
        variants: formData.variants,
        categoryIds: formData.categoryIds
      };

      if (product) {
        await api.put(`/products/${product.publicId}`, requestData);
        showToast('Produto atualizado com sucesso!', 'success');
      } else {
        await api.post('/products', requestData);
        showToast('Produto criado com sucesso!', 'success');
      }
      onSuccess();
      onClose();
    } catch (err: any) {
      console.error('Erro ao salvar produto:', err);
      const errorMsg = err.response?.data?.message || 'Erro ao salvar produto';
      setError(errorMsg);
      showToast(errorMsg, 'error');
    } finally {
      setLoading(false);
    }
  };

  if (loadingData) {
    return (
      <div className="modal-overlay">
        <div className="modal-content">
          <p>Carregando dados...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content product-form" onClick={(e) => e.stopPropagation()}>
        <div className="form-header">
          <h2>{product ? 'Editar Produto' : 'Criar Novo Produto'}</h2>
          <button onClick={onClose} className="btn-close">×</button>
        </div>

        {error && <div className="error-message">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-section">
            <h3>Informações Básicas</h3>
            
            <div className="form-group">
              <label>Nome do Produto *</label>
              <input
                type="text"
                value={formData.name}
                onChange={(e) => handleInputChange('name', e.target.value)}
                required
              />
            </div>

            <div className="form-group">
              <label>Descrição</label>
              <textarea
                value={formData.description}
                onChange={(e) => handleInputChange('description', e.target.value)}
                rows={3}
              />
            </div>

            <div className="form-row">
              <div className="form-group">
                <label>Código ANVISA</label>
                <input
                  type="text"
                  value={formData.anvisaCode}
                  onChange={(e) => handleInputChange('anvisaCode', e.target.value)}
                />
              </div>

              <div className="form-group">
                <label>Princípio Ativo *</label>
                <input
                  type="text"
                  value={formData.activePrinciple}
                  onChange={(e) => handleInputChange('activePrinciple', e.target.value)}
                  required
                />
              </div>
            </div>

            <div className="form-row">
              <div className="form-group">
                <label>Lista de Substâncias Controladas *</label>
                <input
                  type="text"
                  value={formData.controlledSubstanceList}
                  onChange={(e) => handleInputChange('controlledSubstanceList', e.target.value)}
                  required
                />
              </div>

              <div className="form-group">
                <label>
                  <input
                    type="checkbox"
                    checked={formData.isPrescriptionRequired}
                    onChange={(e) => handleInputChange('isPrescriptionRequired', e.target.checked)}
                  />
                  Requer Receita Médica
                </label>
              </div>
            </div>

            <div className="form-row">
              <div className="form-group">
                <label>Marca</label>
                <select
                  value={formData.brandId || ''}
                  onChange={(e) => handleInputChange('brandId', e.target.value ? Number(e.target.value) : null)}
                >
                  <option value="">Selecione uma marca</option>
                  {brands.map(brand => (
                    <option key={brand.id} value={brand.id}>{brand.name}</option>
                  ))}
                </select>
              </div>

              <div className="form-group">
                <label>Fabricante</label>
                <select
                  value={formData.manufacturerId || ''}
                  onChange={(e) => handleInputChange('manufacturerId', e.target.value ? Number(e.target.value) : null)}
                >
                  <option value="">Selecione um fabricante</option>
                  {manufacturers.map(manufacturer => (
                    <option key={manufacturer.id} value={manufacturer.id}>{manufacturer.name}</option>
                  ))}
                </select>
              </div>
            </div>
          </div>

          <div className="form-section">
            <div className="section-header">
              <h3>Variantes do Produto</h3>
              <button type="button" onClick={addVariant} className="btn btn-secondary btn-sm">
                Adicionar Variante
              </button>
            </div>

            {formData.variants.map((variant, index) => (
              <div key={index} className="variant-form">
                <div className="variant-header">
                  <h4>Variante {index + 1}</h4>
                  {formData.variants.length > 1 && (
                    <button
                      type="button"
                      onClick={() => removeVariant(index)}
                      className="btn btn-danger btn-sm"
                    >
                      Remover
                    </button>
                  )}
                </div>

                <div className="form-row">
                  <div className="form-group">
                    <label>SKU *</label>
                    <input
                      type="text"
                      value={variant.sku}
                      onChange={(e) => handleVariantChange(index, 'sku', e.target.value)}
                      required
                    />
                  </div>

                  <div className="form-group">
                    <label>Dosagem *</label>
                    <input
                      type="text"
                      value={variant.dosage}
                      onChange={(e) => handleVariantChange(index, 'dosage', e.target.value)}
                      required
                    />
                  </div>

                  <div className="form-group">
                    <label>Tamanho da Embalagem *</label>
                    <input
                      type="text"
                      value={variant.packageSize}
                      onChange={(e) => handleVariantChange(index, 'packageSize', e.target.value)}
                      required
                    />
                  </div>

                  <div className="form-group">
                    <label>GTIN</label>
                    <input
                      type="text"
                      value={variant.gtin || ''}
                      onChange={(e) => handleVariantChange(index, 'gtin', e.target.value)}
                    />
                  </div>
                </div>
              </div>
            ))}
          </div>

          <div className="form-section">
            <h3>Categorias</h3>
            <div className="categories-list">
              {categories.map(category => (
                <label key={category.id} className="checkbox-label">
                  <input
                    type="checkbox"
                    checked={formData.categoryIds.includes(category.id)}
                    onChange={() => handleCategoryToggle(category.id)}
                  />
                  {category.name}
                </label>
              ))}
            </div>
          </div>

          <div className="form-actions">
            <button type="button" onClick={onClose} className="btn btn-secondary">
              Cancelar
            </button>
            <button type="submit" className="btn btn-primary" disabled={loading}>
              {loading ? 'Salvando...' : (product ? 'Atualizar' : 'Criar')}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ProductForm;

